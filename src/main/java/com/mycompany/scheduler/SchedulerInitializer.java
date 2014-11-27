package com.mycompany.scheduler;

import com.mycompany.scheduler.quartz.MyScheduler;
import java.io.PrintStream;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.quartz.SchedulerException;

@Singleton
@Startup
@DependsOn({"MyScheduler"})
public class SchedulerInitializer
{

  @Inject
  MyScheduler myScheduler;

  @PersistenceContext
  EntityManager em;

  @PostConstruct
  public void init()
  {
    System.out.println("SchedulerInitializer initialized");
    TypedQuery query = this.em.createNamedQuery("Job.findAll", Job.class);
    List<Job> resultList = query.getResultList();

    System.out.println("Aantal jobs: " + resultList.size());
    for (Job j : resultList)
      try
      {
        System.out.println("About to schedule job " + j);
        this.myScheduler.schedule(j);
      } catch (SchedulerException ex) {
        System.out.println("Failed to schedule job " + j + ". " + ex);
      } catch (Throwable t) {
        System.out.println("Ooops " + t);
      }
  }
}