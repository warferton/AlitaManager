package com.alexkirillov.alitamanager.api.helpers;

import com.alexkirillov.alitamanager.api.EmployeeController;
import com.alexkirillov.alitamanager.api.WorkdayController;
import com.alexkirillov.alitamanager.dao.appointmentrepo.AppointmentRepository;
import com.alexkirillov.alitamanager.models.Appointment;
import com.alexkirillov.alitamanager.models.Employee;
import com.alexkirillov.alitamanager.models.Workday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

@Component
public class AppointmentControllerHelper {
    private WorkdayController workdayController;
    private EmployeeController employeeController;

    @Autowired
    public AppointmentControllerHelper(WorkdayController workdayController, EmployeeController employeeController) {
        this.workdayController = workdayController;
        this.employeeController = employeeController;
    }


    public ResponseEntity<String> insertOneAppointment(Appointment new_appointment,
                                                       AppointmentRepository appointmentRepository) {

        RestTemplate restTemplate = buildRestTemplate();

        Map<String, String> response_headers = new HashMap<>();

        int day_id = Integer.parseInt(new_appointment.getDayId());

        //get specified employees from the request
        List<Employee> request_employees_names = new_appointment.getEmployees();

        //go though the list and retrieve every employee that matched from the db
        //then insert them in a new ArrayList also checking for duplicates
        List<Employee> db_retrieved_employees = new ArrayList<>();
        for(Employee e : request_employees_names){
            Employee employee = employeeController.getByName(e.getFirstName()).get(0);
            if(!db_retrieved_employees.contains(employee))
                db_retrieved_employees.add(employee);
        }

        //get initial response
        List<Workday> workday_res = this.workdayController.getByDayId(day_id + "");


        boolean workday_not_exist_flag = workday_res.isEmpty();

        if (workday_not_exist_flag) {

            this.workdayController.insertWorkday(new Workday(LocalDate.now().plusDays(day_id - 1)));

            ResponseEntity<String> response = this.employeeController
                    .adjustWorkTime(SECRET_KEY.getLoad(),
                            db_retrieved_employees.get(0).getId() + "",
                            new_appointment.getInterval(), day_id - 1 );

            if (response.getStatusCodeValue() == 200) {
                appointmentRepository.insert(new_appointment);

                response_headers.put("intervalStart", new_appointment.getInterval()
                        .getStartTime().toLocalTime().toString());
                response_headers.put("employee", db_retrieved_employees.get(0).getFirstName());

                return new ResponseEntity(response_headers, HttpStatus.CREATED);
            }
        }
        else {

            for(int i = 0; i < db_retrieved_employees.size(); i ++) {

                //check if employee has enough work_time to accept the appointment
                int time_left_flag = db_retrieved_employees.get(i).getWorkdayTime()
                        .compareTo(new_appointment.getService().getEstimatedTime());

                if (time_left_flag > 0) {
                    //DECREMENT TIME LEFT AND INSERT INTERVAL
                    ResponseEntity<String> response = this.employeeController.adjustWorkTime
                            (SECRET_KEY.getLoad(), db_retrieved_employees.get(i).getId() + "",
                                    new_appointment.getInterval(), day_id - 1);

                    if (response.getStatusCodeValue() == 200) {
                        appointmentRepository.insert(new_appointment);

                        response_headers.put("intervalStart", new_appointment.getInterval()
                                .getStartTime().toLocalTime().toString());
                        response_headers.put("employee", db_retrieved_employees.get(i).getFirstName());

                        return new ResponseEntity(response_headers, HttpStatus.CREATED);
                    }
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
