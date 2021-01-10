package com.alexkirillov.alitamanager.dao;

import com.alexkirillov.alitamanager.api.AppointmentController;
import com.alexkirillov.alitamanager.api.WorkdayController;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

public class WorkdayCleaner {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private AppointmentController appointmentController;
    private WorkdayController workdayController;

    @Autowired
    public WorkdayCleaner(AppointmentController appointmentController, WorkdayController workdayController) {
        this.appointmentController = appointmentController;
        this.workdayController = workdayController;
    }



    public void DayUpdate() {

         final Runnable updater = () -> {
             /* updater code
              erase past workdays every day
              */

             try {
                // decrement wds
                 this.workdayController.decrementDayIds(SECRET_KEY.getLoad());
                 //decrement appointments
                 this.appointmentController.decrementDayIds(SECRET_KEY.getLoad());
                 //delete wd
                 this.workdayController.deleteWorkday("0");///TEST OUT LATER



             } catch (Exception e) {
                 e.printStackTrace();
             }

         };

        ScheduledFuture updaterHandlerAtFixedRate =
                scheduler.scheduleAtFixedRate(updater, 24, 24, TimeUnit.HOURS);
    }


}
