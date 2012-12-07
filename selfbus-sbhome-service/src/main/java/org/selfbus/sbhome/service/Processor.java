package org.selfbus.sbhome.service;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import org.selfbus.sbhome.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The processor queues the work it gets and executes it one after another in it's own background
 * thread.
 * 
 * @see #start()
 * @see #stop()
 */
public class Processor
{
   private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

   /**
    * The default priority when scheduling work without priority.
    */
   public static final int DEFAULT_PRIORITY = 10;

   private final Queue<WorkInfo> workQueue = new PriorityQueue<WorkInfo>();
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

      LOGGER.info("Starting processor thread");

      dispatcherThread = new Dispatcher("processor");
      dispatcherThread.start();
   }

   /**
    * Stop the event dispatcher thread.
    */
   public void stop()
   {
      if (dispatcherThread != null)
      {
         LOGGER.info("Stopping processor thread");

         dispatcherThread.running = false;
         dispatcherThread = null;
         workSemaphore.release();
      }
   }

   /**
    * Causes <i>doRun.run()</i> to be executed asynchronously in the processor thread with
    * {@link #DEFAULT_PRIORITY default priority}. This will happen after all other pending events
    * with higher priority have been processed.
    * 
    * @param doRun - the runnable to be executed
    */
   public void invokeLater(Runnable doRun)
   {
      invokeLater(DEFAULT_PRIORITY, doRun);
   }

   /**
    * Causes <i>doRun.run()</i> to be executed asynchronously in the processor thread. This will
    * happen after all other pending events with higher priority have been processed.
    * 
    * @param priority - the priority of the runnable.
    * @param doRun - the runnable to be executed.
    */
   public void invokeLater(int priority, Runnable doRun)
   {
      synchronized (workQueue)
      {
         workQueue.add(new WorkInfo(doRun, priority));
         workSemaphore.release();
      }
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

            WorkInfo workInfo;
            synchronized (workQueue)
            {
               workInfo = workQueue.poll();
            }

            try
            {
               workInfo.runnable.run();
            }
            catch (Exception e)
            {
               LOGGER.error("Uncaught exception in processor", e);
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

   /**
    * Internal helper class of {@link Processor}.
    */
   private static class WorkInfo implements Comparable<WorkInfo>
   {
      public final Runnable runnable;
      public final int priority;

      public WorkInfo(Runnable runnable, int priority)
      {
         this.runnable = runnable;
         this.priority = priority;
      }

      /**
       * Compares this object with the specified object for order. Returns a negative integer, zero,
       * or a positive integer as this object is less than, equal to, or greater than the specified
       * object.
       */
      @Override
      public int compareTo(WorkInfo o)
      {
         return priority - o.priority;
      }
   }
}
