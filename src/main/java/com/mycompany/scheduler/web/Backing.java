package com.mycompany.scheduler.web;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mycompany.scheduler.Job;
import com.mycompany.scheduler.quartz.MyScheduler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.quartz.SchedulerException;

/**
 *
 * @author dickdijk
 */
@Named(value = "backing")
@Dependent
@Stateless
public class Backing {

    String name;
    String schedule;
    
    @Inject
    MyScheduler myScheduler;
    
    
    @PersistenceContext
    EntityManager em;
    /**
     * Creates a new instance of Backing
     */
    public Backing() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
 
    public void save() {
        Job j = new Job(name, schedule);
        
        em.persist(j);
        try {
            myScheduler.schedule(j);
        } catch (SchedulerException ex) {
            System.err.println("Failed to schedule job " + j);
        }
        
    }
    
    public void startNow() {
         Job j = new Job(name, schedule);
        try {
            myScheduler.startNow(j);
        } catch (SchedulerException ex) {
            System.err.println("Failed to schedule job " + j);
        }
    }
    
}
