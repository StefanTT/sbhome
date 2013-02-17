package org.selfbus.sbhome.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.jexl2.JexlEngine;
import org.freebus.fts.common.address.GroupAddress;
import org.freebus.knxcomm.BusInterface;
import org.freebus.knxcomm.BusInterfaceFactory;
import org.freebus.knxcomm.application.ApplicationType;
import org.freebus.knxcomm.application.GenericDataApplication;
import org.freebus.knxcomm.application.GroupValueWrite;
import org.freebus.knxcomm.application.value.DataPointType;
import org.freebus.knxcomm.link.netip.KNXnetLink;
import org.freebus.knxcomm.link.serial.SerialPortException;
import org.freebus.knxcomm.telegram.Telegram;
import org.freebus.knxcomm.telegram.TelegramListener;
import org.freebus.knxcomm.types.LinkMode;
import org.selfbus.sbhome.service.internal.I18n;
import org.selfbus.sbhome.service.misc.Config;
import org.selfbus.sbhome.service.misc.ScriptUtils;
import org.selfbus.sbhome.service.model.Project;
import org.selfbus.sbhome.service.model.ProjectImporter;
import org.selfbus.sbhome.service.model.variable.GroupVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The daemon holds the global objects of the FB-Home server.
 */
public class Daemon
{
   private static final Logger LOGGER = LoggerFactory.getLogger(Daemon.class);
   private static Daemon daemon = null;

   private Project project;
   private String projectFileName;

   private BusInterface busInterface;
   private final JexlEngine jexl = ScriptUtils.createJexlEngine();
   private final Processor processor = new Processor();
   private final Set<GroupTelegramListener> telegramListeners = new CopyOnWriteArraySet<GroupTelegramListener>();

   private final Queue<Telegram> telegramHistory = new ConcurrentLinkedQueue<Telegram>();
   private int telegramHistorySize = 20;

   /**
    * @return The global daemon instance.
    */
   public synchronized static Daemon getInstance()
   {
      if (daemon == null)
      {
         new Daemon();
      }
      return daemon;
   }

   /**
    * Create a daemon instance.
    * 
    * @see #getInstance()
    */
   public Daemon()
   {
      daemon = this;

      LOGGER.debug("Daemon created");
      setupBusInterface();
      processor.start();
   }

   /**
    * Create a daemon instance.
    * 
    * @param projectFileName - the name of the project file
    *
    * @see #getInstance()
    */
   public Daemon(String projectFileName)
   {
      this();

      try
      {
         loadProject(projectFileName);
      }
      catch (FileNotFoundException e)
      {
         LOGGER.error("failed to load project.xml file", e);
         throw new RuntimeException(e);
      }
   }

   /**
    * Create the bus interface and setup the telegram listeners.
    */
   void setupBusInterface()
   {
      BusInterface iface = null;

      Config cfg = Config.getInstance();
      String type = cfg.getStringValue("busInterface.type");

      if ("ip".equals(type))
      {
         String host = cfg.getStringValue("busInterface.host");
         int port = cfg.getIntValue("busInterface.port", KNXnetLink.defaultPortUDP);

         iface = BusInterfaceFactory.newKNXnetInterface(host, port);
         LOGGER.info("Using KNXnet/IP bus interface: {0} port {1}", host, port);
      }
      else
      {
         iface = BusInterfaceFactory.newDummyInterface();
         LOGGER.info("Using dummy bus interface");
      }

      try
      {
         iface.open(LinkMode.BusMonitor);
      }
      catch (SerialPortException | IOException e)
      {
         LOGGER.warn(e.getMessage());

         LOGGER.info("Using simulated bus interface");
         iface = null; // BusInterfaceFactory.newDummyInterface();
      }

      setBusInterface(iface);
   }

   /**
    * @return The project.
    */
   public Project getProject()
   {
      return project;
   }

   /**
    * @return The work processor.
    * 
    * @see #invokeLater(Runnable)
    */
   public Processor getProcessor()
   {
      return processor;
   }

   /**
    * Causes <i>doRun.run()</i> to be executed asynchronously in the event dispatching thread. This
    * will happen after all pending events have been processed.
    * 
    * @param doRun - the runnable to be executed
    */
   public void invokeLater(Runnable doRun)
   {
      processor.invokeLater(doRun);
   }

   /**
    * @return The daemon's script engine.
    */
   public JexlEngine getScriptEngine()
   {
      return jexl;
   }

   /**
    * Send a telegram.
    * 
    * @param telegram - the telegram to send.
    */
   protected void sendTelegram(Telegram telegram)
   {
      if (busInterface != null)
      {
         try
         {
            busInterface.send(telegram);
         }
         catch (IOException e)
         {
            throw new RuntimeException(I18n.formatMessage("Daemon.errSendTelegram", e.getMessage()));
         }
      }
      else
      {
         telegramListener.telegramSent(telegram);
      }
   }

   /**
    * Send a {@link ApplicationType#GroupValue_Write group-value write} telegram.
    * 
    * @param dest - the destination group address
    * @param dataType - the data type
    * @param data - the data value
    * @param fireEvents - shall the telegram sending trigger group-variable events?
    */
   public void sendTelegram(GroupAddress dest, DataPointType dataType, byte[] data, boolean fireEvents)
   {
      GroupValueWrite app = new GroupValueWrite();

      if (dataType.isUsingApci())
         app.setApciData(data);
      else app.setData(data);

      Telegram telegram = new Telegram(app);
      telegram.setDest(dest);

      if (!fireEvents)
         telegram.setUserData("noEvents");

      sendTelegram(telegram);
   }

   /**
    * @return The telegram history. This history stores the latest x telegrams.
    */
   public Queue<Telegram> getTelegramHistory()
   {
      return telegramHistory;
   }

   /**
    * Add a telegram to the history.
    * 
    * @param telegram - the telegram to add.
    */
   protected synchronized void addToTelegramHistory(Telegram telegram)
   {
      while (telegramHistory.size() > telegramHistorySize)
         telegramHistory.poll();

      telegramHistory.add(telegram.clone());
   }

   /**
    * Load the project.
    * 
    * @param fileName - the name of the project file to load.
    * 
    * @throws FileNotFoundException if the project was not found
    */
   public void loadProject(final String fileName) throws FileNotFoundException
   {
      final InputStream in = getClass().getResourceAsStream('/' + fileName);
      if (in == null)
      {
         throw new FileNotFoundException("File not found in class path: " + fileName);
      }

      ProjectImporter importer = new ProjectImporter();
      project = importer.readProject(in);
      projectFileName = fileName;

      postLoadProject();
   }

   /**
    * Things to be done after loading a project.
    * 
    * Called by {@link #loadProject(String)}.
    */
   protected void postLoadProject()
   {
      processor.setProject(project);
   }

   /**
    * @return The project file.
    */
   public File getProjectFile()
   {
      return projectFileName == null ? null : new File(projectFileName);
   }

   /**
    * @return The bus interface.
    */
   public BusInterface getBusInterface()
   {
      return busInterface;
   }

   /**
    * Set the bus interface.
    * 
    * @param iface - the bus interface to set
    */
   public void setBusInterface(BusInterface iface)
   {
      if (this.busInterface != null)
         this.busInterface.removeListener(telegramListener);

      this.busInterface = iface;

      if (this.busInterface != null)
         this.busInterface.addListener(telegramListener);
   }

   /**
    * Register a telegram listener.
    * 
    * @param listener - the listener to add
    */
   public void addTelegramListener(GroupTelegramListener listener)
   {
      telegramListeners.add(listener);
   }

   /**
    * Unregister a telegram listener.
    * 
    * @param listener - the listener to remove
    */
   public void removeTelegramListener(GroupTelegramListener listener)
   {
      telegramListeners.remove(listener);
   }

   /**
    * Inform all telegram listeners about a telegram.
    * 
    * @param telegram - the telegram.
    */
   public void fireTelegramReceived(Telegram telegram)
   {
      for (GroupTelegramListener listener : telegramListeners)
         listener.telegramReceived(telegram);
   }

   /**
    * Inform all telegram listeners about a telegram.
    * 
    * @param telegram - the telegram.
    */
   public void fireTelegramSent(Telegram telegram)
   {
      for (GroupTelegramListener listener : telegramListeners)
         listener.telegramSent(telegram);
   }

   /**
    * Get the group variable for the telegram.
    * 
    * @param telegram - the telegram
    * @return The group variable, or null if not found
    */
   protected GroupVariable getVariable(Telegram telegram)
   {
      GroupAddress addr = (GroupAddress) telegram.getDest();

      GroupVariable var = project.getVariable(addr);
      if (var == null)
         LOGGER.debug("Ignoring telegram for unkown group {}", addr);

      return var;
   }

   /**
    * The internal telegram listener.
    */
   private final TelegramListener telegramListener = new TelegramListener()
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public void telegramSent(final Telegram telegram)
      {
         if (!(telegram.getDest() instanceof GroupAddress))
            return;

         LOGGER.debug("Telegram sent: {}", telegram);
         addToTelegramHistory(telegram);

         GroupVariable var = getVariable(telegram);
         if (var != null && var.isRead())
         {
            GenericDataApplication app = (GenericDataApplication) telegram.getApplication();
            var.setRawValue(app.getApciData(), !"noEvents".equals(telegram.getUserData()));
         }

         processor.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               LOGGER.debug("Telegram event: {}", telegram);
               fireTelegramSent(telegram);
            }
         });
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void telegramReceived(final Telegram telegram)
      {
         if (!(telegram.getDest() instanceof GroupAddress))
            return;

         LOGGER.debug("Telegram received: {}", telegram);
         addToTelegramHistory(telegram);

         GroupVariable var = getVariable(telegram);
         if (var != null)
         {
            GenericDataApplication app = (GenericDataApplication) telegram.getApplication();
            var.setRawValue(app.getApciData(), !"noEvents".equals(telegram.getUserData()));
         }

         processor.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               LOGGER.debug("Telegram event: {}", telegram);
               fireTelegramReceived(telegram);
            }
         });
      }
   };
}
