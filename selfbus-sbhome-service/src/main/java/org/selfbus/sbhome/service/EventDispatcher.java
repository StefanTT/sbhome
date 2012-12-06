package org.selfbus.sbhome.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import org.selfbus.sbhome.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The event dispatcher sends the events to all listeners that have registered for a specific event.
 * The actual dispatching of the events happens asynchronous in an extra dispatcher thread.
 * 
 * @see #start()
 * @see #stop()
 */
public class EventDispatcher
{
   private static final Logger LOGGER = LoggerFactory.getLogger(EventDispatcher.class);

   private final Queue<Object> workQueue = new ConcurrentLinkedQueue<Object>();
   private final Semaphore workSemaphore = new Semaphore(0);
   private Dispatcher dispatcherThread;
   private Project project;

   /**
    * Start the event dispatcher thread.
    */
   public void start()
   {
      if (dispatcherThread != null)
         stop();

      LOGGER.info("Starting event dispatcher thread");

      dispatcherThread = new Dispatcher("event-dispatcher");
      dispatcherThread.start();
   }

   /**
    * Stop the event dispatcher thread.
    */
   public void stop()
   {
      if (dispatcherThread != null)
      {
         LOGGER.info("Stopping event dispatcher thread");

         dispatcherThread.running = false;
         dispatcherThread = null;
         workSemaphore.release();
      }
   }

   /**
    * Causes <i>doRun.run()</i> to be executed asynchronously in the event dispatching thread. This
    * will happen after all pending events have been processed.
    * 
    * @param doRun - the runnable to be executed
    */
   public void invokeLater(Runnable doRun)
   {
      workQueue.add(doRun);
      workSemaphore.release();
   }

   /**
    * The class for the event dispatcher thread.
    */
   private class Dispatcher extends Thread
   {
      public boolean running = true;

      public Dispatcher(String name)
      {
         super(name);
      }

      @Override
      public void run()
      {
         while (running)
         {
            try
            {
               workSemaphore.acquire();
            }
            catch (InterruptedException e)
            {
               LOGGER.error("interrupted", e);
            }

            final Object work = workQueue.poll();

            try
            {
               if (work instanceof Runnable)
               {
                  Runnable doRun = (Runnable) work;
                  doRun.run();
               }
               else if (work != null)
               {
                  LOGGER.error("Ignoring unknown work payload {}", work.getClass().getName());
               }
            }
            catch (Exception e)
            {
               LOGGER.error("Uncaught exception in event dispatcher", e);
            }
         }
      }
   }

   /**
    * @return the project
    */
   public Project getProject()
   {
      return project;
   }

   /**
    * Set the project
    * 
    * @param project - the project to set
    */
   public void setProject(Project project)
   {
      this.project = project;
   }
}
