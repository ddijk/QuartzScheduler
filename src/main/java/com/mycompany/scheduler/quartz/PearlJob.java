package com.mycompany.scheduler.quartz;

import java.io.PrintStream;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class PearlJob
  implements Job
{
  String id;
  private static int counter;
  
  String env;

  public void execute(JobExecutionContext jec)
    throws JobExecutionException
  {
    System.out.println("Running job " + jec.getJobDetail().getDescription() + " ** " + counter++ + ". " + new Date()+", env="+jec.getMergedJobDataMap().getString("env"));
  }

  public String toString()
  {
    return "job met id " + this.id + ", at " + new Date();
  }
}