package com.alexkirillov.alitamanager.api.helpers;

import com.alexkirillov.alitamanager.dao.appointmentrepo.AppointmentRepository;
import com.alexkirillov.alitamanager.models.Appointment;
import com.alexkirillov.alitamanager.models.Workday;
import org.junit.Assert;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

public class AppointmentControllerHelper {


    private String insert_workdays_url= "https://fast-sierra-37663.herokuapp.com/api/schedule/workdays/add/one/";
    private String get_specific_workday_url = "https://fast-sierra-37663.herokuapp.com/api/schedule/workdays/find/byDayId/";//<== dayId here
    private String adjust_time_url = "https://fast-sierra-37663.herokuapp.com/api/schedule/workdays/secret/"+SECRET_KEY.getLoad()+"/time/adjust/dayId/";

    public ResponseEntity insertOneAppointment(Appointment new_appointment, AppointmentRepository appointmentRepository) {
        RestTemplate restTemplate = buildRestTemplate();
        int id = Integer.parseInt(new_appointment.getDayId());

        //get initial response
        ResponseEntity<String> workday_result =
                restTemplate.getForEntity(get_specific_workday_url + id, String.class);
        //Verify request succeed
        Assert.assertEquals(200, workday_result.getStatusCodeValue());

        //checking if there is already a workday with the same day_id as the new appointment

        boolean workady_not_exsit_flag = workday_result.getBody().replace("[]", "").isEmpty();


        if (workady_not_exsit_flag) {

            restTemplate.put(insert_workdays_url, new Workday(LocalDate.now().plusDays(id - 1)));
            ResponseEntity<String> response = restTemplate.postForEntity(adjust_time_url + id, new_appointment.getInterval(), String.class);

            if (response.getStatusCodeValue() == 200) {
                appointmentRepository.insert(new_appointment);
                return new ResponseEntity(HttpStatus.CREATED);
            }
        }
        else {
            //get the workday_time from response body
            String body = workday_result.getBody();
            Duration time_left = Duration.parse(
                    body.substring(
                            body.indexOf("PT"),
                            ((body.indexOf("M") > 0) ? body.indexOf("M") : body.indexOf("H")) + 1
                    )
            );
            //check if workday has enough time to accept the appointment

            int time_left_flag = time_left.compareTo(new_appointment.getService().getEstimatedTime());

            if (time_left_flag > 0) {
                //DECREMENT TIME LEFT AND INSERT INTERVAL
                ResponseEntity<String> response = restTemplate.postForEntity(adjust_time_url + id, new_appointment.getInterval(), String.class);
                if (response.getStatusCodeValue() == 200) {
                    appointmentRepository.insert(new_appointment);
                    return new ResponseEntity(HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity(HttpStatus.INSUFFICIENT_STORAGE);
    }

    private RestTemplate buildRestTemplate(){
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder.build();
    }


}
