package com.mycompany.scheduler.quartz.example;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob
  implements Job
{
  Logger _log = LoggerFactory.getLogger(SimpleJob.class);

  public void execute(JobExecutionContext context) throws JobExecutionException
  {
    JobKey jobKey = context.getJobDetail().getKey();
    this._log.info("SimpleJob says: " + jobKey + " executing at " + new Date());
  }
}