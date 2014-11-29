package com.mycompany.scheduler.quartz;

import com.mycompany.scheduler.Job;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

@Singleton
@Startup
public class MyScheduler
  implements Serializable
{
  Scheduler sched;

  @PostConstruct
  void init()
  {
    SchedulerFactory sf = new StdSchedulerFactory();
    try {
      this.sched = sf.getScheduler();
      this.sched.start();
      System.out.println("Scheduler started");
    } catch (SchedulerException ex) {
      Logger.getLogger(MyScheduler.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException("Failed to create Scheduler.  " + ex, ex);
    }
  }

  @Deprecated
  public void scheduleJob(JobDetail jd, Trigger t) throws SchedulerException
  {
    this.sched.scheduleJob(jd, t);
    System.out.println("Job scheduled at " + new Date());
  }

  public void unscheduleJob(TriggerKey tk)
  {
    try {
      this.sched.unscheduleJob(tk);
      System.out.println("Trgger unscheduled");
    } catch (SchedulerException ex) {
      System.out.println("Failed to remove triggerKey");
    }
  }

  @PreDestroy
  public void shutdown()
  {
    try {
      System.out.println("Going down, shutting down scheduler");
      this.sched.shutdown();
      System.out.println("Completed Scheduler shutdown");
    } catch (SchedulerException ex) {
      System.out.println("Shutdown failed. " + ex);
    }
  }

  public void schedule(Job j) throws SchedulerException
  {
    Trigger trigger = createTrigger(j);

    JobDetail job = JobBuilder.newJob(PearlJob.class)
      .withIdentity(" "+j.getId())
    // .getName(), "group1")
      .withDescription(j.getName())
            .usingJobData("env", "DEV1")
    //  .getName())
      .build();
    this.sched.scheduleJob(job, trigger);
  }

  private Trigger createTrigger(Job j)
  {
    return TriggerBuilder.newTrigger()
      .withIdentity("trigger" + j)
  //    .getId(), "group1")
      .withSchedule(CronScheduleBuilder.cronSchedule(j.getSchedule()))
     // .getSchedule()
      .build();
  }
  
   private Trigger createNowTrigger()
  {
    return TriggerBuilder.newTrigger()
      .withIdentity("nowtrigger")
            .startNow()
      .build();
  }

  private Trigger createTrigger(String name, String cronSchedule)
  {
    Date runTime = DateBuilder.evenMinuteDate(new Date());

    return TriggerBuilder.newTrigger()
      .withIdentity(name, "group1")
      .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(10, 4))
      .build();
  }
  
  public void startNow(Job j) throws SchedulerException {
      
      
      JobDetail job = JobBuilder.newJob(PearlJob.class)
      .withIdentity(" "+j.getId())
    // .getName(), "group1")
      .withDescription(j.getName())
            .usingJobData("env", "DEV2")
    //  .getName())
      .build();
    this.sched.scheduleJob(job, createNowTrigger());
  }
}