package org.selfbus.sbhome.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;
import org.freebus.fts.common.address.Address;
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
import org.selfbus.sbhome.internal.I18n;
import org.selfbus.sbhome.misc.ScriptUtils;
import org.selfbus.sbhome.model.Project;
import org.selfbus.sbhome.model.ProjectImporter;
import org.selfbus.sbhome.model.module.AbstractProgramConnector;
import org.selfbus.sbhome.model.module.Program;
import org.selfbus.sbhome.model.variable.Variable;
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
   private BusInterface busInterface;
   private final JexlEngine jexl = ScriptUtils.createJexlEngine();
   private final EventDispatcher eventDispatcher = new EventDispatcher();
   private final Queue<Telegram> telegramHistory = new ConcurrentLinkedQueue<Telegram>();
   private int historySize = 20;

   /**
    * @return The global daemon instance.
    */
   public synchronized static Daemon getInstance()
   {
      if (daemon == null)
      {
         daemon = new Daemon();
      }
      return daemon;
   }

   /**
    * Create a daemon instance.
    * 
    * @see #getInstance()
    */
   Daemon()
   {
      LOGGER.debug("Daemon created");

      setupBusInterface();

      try
      {
         loadProject("example-project.xml");
      }
      catch (FileNotFoundException e)
      {
         LOGGER.error("failed to load project.xml file", e);
         throw new RuntimeException(e);
      }

      eventDispatcher.setProject(project);
      eventDispatcher.start();
   }

   /**
    * Create the bus interface and setup the telegram listeners.
    */
   void setupBusInterface()
   {
      final String portName = "/dev/ttyUSB0";
      BusInterface iface;

      try
      {
         //iface = BusInterfaceFactory.newSerialInterface(portName);
         //LOGGER.info("Using serial bus interface, port {}", portName);

         iface = BusInterfaceFactory.newKNXnetInterface("localhost", KNXnetLink.defaultPortUDP);
         iface.open(LinkMode.BusMonitor);

         LOGGER.info("Using KNXnet/IP bus interface");
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
    * @return The event dispatcher.
    * 
    * @see runLater
    */
   public EventDispatcher getEventDispatcher()
   {
      return eventDispatcher;
   }

   /**
    * Causes <i>doRun.run()</i> to be executed asynchronously in the event dispatching thread. This
    * will happen after all pending events have been processed.
    * 
    * @param doRun - the runnable to be executed
    */
   public void invokeLater(Runnable doRun)
   {
      eventDispatcher.invokeLater(doRun);
   }

   /**
    * Send a telegram.
    * 
    * @param telegram - the telegram to send.
    */
   protected void sendTelegram(Telegram telegram)
   {
      if (telegram.getDest() != null && busInterface != null)
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
         if (telegram.getDest() instanceof GroupAddress)
            storeGroupValue(telegram);

         eventDispatcher.telegramReceived(telegram);
      }
   }

   /**
    * Send a {@link ApplicationType#GroupValue_Write group-value write} telegram
    * 
    * @param dest - the destination group address
    * @param dataType - the data type
    * @param data - the data value
    */
   public void sendTelegram(GroupAddress dest, DataPointType dataType, byte[] data)
   {
      GroupValueWrite app = new GroupValueWrite();

      if (dataType.isUsingApci())
         app.setApciData(data);
      else app.setData(data);

      Telegram telegram = new Telegram(app);
      telegram.setDest(dest);

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
    * If the telegram is a {@link ApplicationType#GroupValue_Write} or
    * {@link ApplicationType#GroupValue_Response} telegram, then store the value of the telegram in
    * the internal group-value cache.
    * 
    * @param telegram - the telegram
    */
   protected synchronized void storeGroupValue(Telegram telegram)
   {
      ApplicationType type = telegram.getApplicationType();

      Address dest = telegram.getDest();
      if (!(dest instanceof GroupAddress) || project == null)
         return;

      telegramHistory.add(telegram.clone());

      while (telegramHistory.size() > historySize)
         telegramHistory.poll();

      if (type.equals(ApplicationType.GroupValue_Write) || type.equals(ApplicationType.GroupValue_Response))
      {
         GenericDataApplication app = (GenericDataApplication) telegram.getApplication();

         Variable group = project.getVariable((GroupAddress) dest);
         if (group == null)
            return;

         if (group.getType().isUsingApci())
            group.setRawValue(app.getApciData());
         else group.setRawValue(app.getData());
      }
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

      final ProjectImporter importer = new ProjectImporter();
      final Project project = importer.readProject(in);

      this.project = project;
      initPrograms();
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
    * Initialize the project's programs.
    */
   public void initPrograms()
   {
      for (Program program : project.getPrograms())
         initProgram(program);
   }

   /**
    * Initialize a program.
    * 
    * @param program - the program to initialize
    */
   protected void initProgram(Program program)
   {
      Expression expr = jexl.createExpression(program.getCode());
      program.setExpression(expr);

      for (AbstractProgramConnector connector : program.getConnectors())
      {
         // TODO
      }
   }

   private final TelegramListener telegramListener = new TelegramListener()
   {
      @Override
      public void telegramSent(Telegram telegram)
      {
         LOGGER.debug("Telegram sent: {}", telegram);
         if (telegram.getDest() instanceof GroupAddress)
            storeGroupValue(telegram);
      }

      @Override
      public void telegramReceived(Telegram telegram)
      {
         LOGGER.debug("Telegram received: {}", telegram);
         if (telegram.getDest() instanceof GroupAddress)
         {
            storeGroupValue(telegram);
            eventDispatcher.telegramReceived(telegram);
         }
      }
   };
}
