package com.alexkirillov.alitamanager.dao;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WorkdayCleaner {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final String key = "key";
    private final String delete_workday_url= "http://localhost:8080/api/schedule/workdays/delete/dayId/1";
    private final String decrement_wd_dayid_url = "http://localhost:8080/api/schedule/workdays/secret/{key}/dec/all";
    private final String decrement_ap_dayid_url = "http://localhost:8080/api/schedule/appointments/secret/{key}/dec/all";


    public void DayUpdate() {
         RestTemplate restTemplate = buildRestTemplate();

         final Runnable updater = () -> {
             /* updater code
              erase past workdays every day
              */

             try {
                // decrement wds
                 restTemplate.delete(decrement_wd_dayid_url, key);
                 //decrement appointments
                 restTemplate.delete(decrement_ap_dayid_url, key);
                 //delete wd
                 restTemplate.delete(delete_workday_url, key);



             } catch (Exception e) {
                 e.printStackTrace();
             }

         };

        ScheduledFuture updaterHandlerAtFixedRate =
                scheduler.scheduleAtFixedRate(updater, 24, 24, TimeUnit.HOURS);
    }

    private RestTemplate buildRestTemplate(){
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder.build();
    }


}
