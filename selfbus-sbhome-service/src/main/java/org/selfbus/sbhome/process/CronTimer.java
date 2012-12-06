package org.selfbus.sbhome.process;

import java.util.Calendar;
import java.util.Date;

import org.selfbus.sbhome.misc.FixedPeriodCron;

/**
 * Information for scheduling a cron trigger.
 */
public class CronTimer
{
   private Date lastRun, nextRun;
   private final FixedPeriodCron cron;

   /**
    * Create a cron timer.
    * 
    * @param cronExpr - the cron expression
    */
   public CronTimer(String cronExpr)
   {
      cron = new FixedPeriodCron(cronExpr);
      calcNextRun();
   }

   /**
    * @return The date for the next scheduled execution or null if no next run is scheduled.
    */
   public Date getNextRun()
   {
      return nextRun;
   }

   /**
    * @return The date for the last execution, or null if never executed.
    */
   public Date getLastRun()
   {
      return lastRun;
   }

   /**
    * Set the timer to the next scheduled execution, and set the last execution to now.
    */
   public void executed()
   {
      lastRun = Calendar.getInstance().getTime();
      calcNextRun();
   }

   /**
    * Calculate the date for the next run.
    */
   protected void calcNextRun()
   {
      Calendar now = Calendar.getInstance();
      now.add(Calendar.MILLISECOND, 1);

      Long nextMatch = cron.nextMatchInMillis(now);
      if (nextMatch == null)
         nextRun = null;
      else nextRun = new Date(now.getTimeInMillis() + nextMatch);
   }
}
