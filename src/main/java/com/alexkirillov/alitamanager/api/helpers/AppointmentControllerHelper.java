package com.alexkirillov.alitamanager.api.helpers;

import com.alexkirillov.alitamanager.api.WorkdayController;
import com.alexkirillov.alitamanager.dao.appointmentrepo.AppointmentRepository;
import com.alexkirillov.alitamanager.models.Appointment;
import com.alexkirillov.alitamanager.models.Workday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

@Component
public class AppointmentControllerHelper {
    private WorkdayController workdayController;
    @Autowired
    public AppointmentControllerHelper(WorkdayController workdayController) {
        this.workdayController = workdayController;
    }


    public ResponseEntity<String> insertOneAppointment(Appointment new_appointment, AppointmentRepository appointmentRepository) {
        RestTemplate restTemplate = buildRestTemplate();
        int id = Integer.parseInt(new_appointment.getDayId());

        //get initial response
        List<Workday> workday_res = this.workdayController.getByDayId(id + "");

        boolean workady_not_exsit_flag = workday_res.isEmpty();//workday_result.getBody().replace("[]", "").isEmpty();


        if (workady_not_exsit_flag) {


            this.workdayController.insertWorkday(new Workday(LocalDate.now().plusDays(id - 1)));

            ResponseEntity<String> response = this.workdayController.adjustWorkTime(SECRET_KEY.getLoad(), id + "", new_appointment.getInterval());

            if (response.getStatusCodeValue() == 200) {
                appointmentRepository.insert(new_appointment);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
        else {

            //check if workday has enough time to accept the appointment

            int time_left_flag = workday_res.get(0).getWorkdayTime()
                    .compareTo(new_appointment.getService().getEstimatedTime());

            if (time_left_flag > 0) {
                //DECREMENT TIME LEFT AND INSERT INTERVAL
                ResponseEntity<String> response = this.workdayController.adjustWorkTime
                        (SECRET_KEY.getLoad(), id+"", new_appointment.getInterval());

                if (response.getStatusCodeValue() == 200) {
                    appointmentRepository.insert(new_appointment);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
    }

    private RestTemplate buildRestTemplate(){
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder.build();
    }


}
