package org.selfbus.sbhome.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.freebus.fts.common.address.Address;
import org.freebus.fts.common.address.GroupAddress;
import org.freebus.knxcomm.BusInterface;
import org.freebus.knxcomm.BusInterfaceFactory;
import org.freebus.knxcomm.application.ApplicationType;
import org.freebus.knxcomm.application.GenericDataApplication;
import org.freebus.knxcomm.link.serial.SerialPortException;
import org.freebus.knxcomm.telegram.Telegram;
import org.freebus.knxcomm.telegram.TelegramListener;
import org.selfbus.sbhome.model.Project;
import org.selfbus.sbhome.model.ProjectImporter;
import org.selfbus.sbhome.model.group.Group;
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

      try
      {
         busInterface = BusInterfaceFactory.newSerialInterface(portName);
         LOGGER.info("Using serial bus interface, port {}", portName);
      }
      catch (SerialPortException e)
      {
         LOGGER.warn(e.getMessage());

         LOGGER.info("Using simulated bus interface");
         busInterface = BusInterfaceFactory.newDummyInterface();
      }

      busInterface.addListener(new TelegramListener()
      {
         @Override
         public void telegramSent(Telegram telegram)
         {
            if (telegram.getDest() instanceof GroupAddress)
               storeGroupValue(telegram);
         }

         @Override
         public void telegramReceived(Telegram telegram)
         {
            if (telegram.getDest() instanceof GroupAddress)
            {
               storeGroupValue(telegram);
               eventDispatcher.telegramReceived(telegram);
            }
         }
      });
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
    */
   public EventDispatcher getEventDispatcher()
   {
      return eventDispatcher;
   }

   /**
    * Send a telegram.
    * 
    * @param telegram - the telegram to send.
    */
   public void sendTelegram(Telegram telegram)
   {
      // TODO implement real sending

      if (telegram.getDest() instanceof GroupAddress)
         storeGroupValue(telegram);

      eventDispatcher.telegramReceived(telegram);
   }

   /**
    * If the telegram is a {@link ApplicationType#GroupValue_Write} or
    * {@link ApplicationType#GroupValue_Response} telegram, then store the value of the telegram in
    * the internal group-value cache.
    * 
    * @param telegram - the telegram
    */
   protected void storeGroupValue(Telegram telegram)
   {
      ApplicationType type = telegram.getApplicationType();
      Address dest = telegram.getDest();

      if (project != null && dest instanceof GroupAddress
         && (type.equals(ApplicationType.GroupValue_Write) || type.equals(ApplicationType.GroupValue_Response)))
      {
         GenericDataApplication app = (GenericDataApplication) telegram.getApplication();
         Group group = project.getGroup((GroupAddress) dest);

         if (group != null)
            group.setValue(app.getData());
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
   }

   /**
    * @return The bus interface.
    */
   public BusInterface getBusInterface()
   {
      return busInterface;
   }
}
