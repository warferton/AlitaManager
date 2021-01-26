package com.alexkirillov.alitamanager.api.helpers;

import com.alexkirillov.alitamanager.api.AppointmentController;
import com.alexkirillov.alitamanager.api.EmployeeController;
import com.alexkirillov.alitamanager.api.WorkdayController;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

@Component
public class WorkdayCleaner {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static AppointmentController appointmentController;
    private static WorkdayController workdayController;
    private static EmployeeController employeeController;

    @Autowired
    public WorkdayCleaner(AppointmentController appointmentController,
                          WorkdayController workdayController,
                          EmployeeController employeeController) {
        WorkdayCleaner.appointmentController = appointmentController;
        WorkdayCleaner.workdayController = workdayController;
        WorkdayCleaner.employeeController = employeeController;
    }



    public static void DayUpdate() {

         final Runnable updater = () -> {
             /* updater code
              erase past workdays every day
              */

             try {
                // decrement wds
                 workdayController.decrementDayIds(SECRET_KEY.getLoad());
                 //decrement appointments
                 appointmentController.decrementDayIds(SECRET_KEY.getLoad());
                 //update employees' schedules
                 employeeController.shiftSchedules();


             } catch (Exception e) {
                 e.printStackTrace();
             }

         };

        ScheduledFuture updaterHandlerAtFixedRate =
                scheduler.scheduleAtFixedRate(updater, 24, 24, TimeUnit.HOURS);
    }


}
