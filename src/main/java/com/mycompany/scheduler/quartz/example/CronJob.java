package com.mycompany.scheduler.quartz.example;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class CronJob
{
  public static void main(String[] args)
    throws SchedulerException, InterruptedException
  {
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    JobDetail job = JobBuilder.newJob(SimpleJob.class)
      .withIdentity("job1", "group1")
      .build();

    CronTrigger trigger = (CronTrigger)TriggerBuilder.newTrigger()
      .withIdentity("trigger1", "group1")
      .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?"))
      .build();
    sched.scheduleJob(job, trigger);

    job = JobBuilder.newJob(SimpleJob.class)
      .withIdentity("job2", "group1")
      .build();

    trigger = (CronTrigger)TriggerBuilder.newTrigger()
      .withIdentity("trigger2", "group1")
      .withSchedule(CronScheduleBuilder.cronSchedule("15 0/2 * * * ?"))
      .build();
    sched.scheduleJob(job, trigger);
    sched.start();

    Thread.sleep(300000L);
    sched.shutdown();
  }
}