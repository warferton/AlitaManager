package com.alexkirillov.alitamanager.api;

import com.alexkirillov.alitamanager.api.helpers.AppointmentControllerHelper;
import com.alexkirillov.alitamanager.dao.appointmentrepo.AppointmentRepository;
import com.alexkirillov.alitamanager.models.Appointment;
import com.alexkirillov.alitamanager.models.QAppointment;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

@RestController
@CrossOrigin(origins = {"https://alita-manager-app.herokuapp.com"})
@RequestMapping(value = {"/api/schedule/appointments"})
public class AppointmentController {

    private AppointmentRepository appointmentRepository;

    private AppointmentControllerHelper helper;

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository, AppointmentControllerHelper helper){
        this.appointmentRepository = appointmentRepository;
        this.helper = helper;
    }


    //CRUD
    @GetMapping(value = {"", "/"})
    @PreAuthorize("hasAuthority('appointments:read')")
    public List<Appointment> getAllAppointments(){
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments;
    }

    @PostMapping(value = {"/update/", "/update"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateAppointment(@RequestBody Appointment new_appointment){
        this.appointmentRepository.save(new_appointment);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping(value = {"/add/one/", "/add/one"})
    public ResponseEntity<String> insertAppointment(@RequestBody Appointment new_appointment){
        return helper.insertOneAppointment(new_appointment, this.appointmentRepository);
    }

//TODO NO FrontEnd implementation as of yet

//    @PutMapping(value = {"/add/many/", "/add/many"})
//    public void insertAppointmentList(@RequestBody List<Appointment> new_appointment_list){
//        this.appointmentRepository.insert(new_appointment_list);
//    }

    @DeleteMapping(value = {"/delete/id/{id}","/delete/id/{id}/"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") String id){
        this.appointmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    //SERVER-SIDE MANIPULATIONS
    @DeleteMapping(value = "secret/{passKey}/dec/all")
    public void decrementDayIds(@PathVariable String passKey){

        if(passKey.equals(SECRET_KEY.getLoad())) {
            List<Appointment> appointments = this.appointmentRepository.findAll();
            this.appointmentRepository.deleteAll();
            appointments.stream()
                    .forEach(a -> a.setDay_id(Integer.parseInt(a.getDayId()) - 1));
            appointments.removeIf(appointment ->
                    LocalDate.now().isAfter(LocalDate.parse(appointment.getDateTime()
                            .substring(0,appointment.getDateTime().indexOf('T')))));
            this.appointmentRepository.saveAll(appointments);
            return;
        }
        throw new AccessDeniedException("Denied access to this resource : key didn't match");
    }

    //TODO Search (NOT USED YET)
//
    @GetMapping(value = {"/find/name/{name}","/find/name/{name}/"})
    public List<Appointment> getByName(@PathVariable("name") String service_name){
        QAppointment appointment = new QAppointment("appointment");
        BooleanExpression filter_name = appointment.service.service_name.contains(service_name);

        return (List<Appointment>) this.appointmentRepository.findAll(filter_name);
    }
//
//    @GetMapping(value = {"/find/price/{price}","/find/price/{price}/"})
//    public List<Appointment> getByPrice(@PathVariable("price") String price){
//        QAppointment appointment = new QAppointment("appointment");
//        BooleanExpression filter_price = appointment.service.price.eq(Double.valueOf(price));
//
//        return (List<Appointment>) this.appointmentRepository.findAll(filter_price);
//    }
//TODO Employee click show all/ show on my name and it displays either all appointments or
//    only those that are for the employee
//
//    @GetMapping(value = {"/find/employee/{employee}","/find/employee/{employee}/"})
//    public List<Appointment> getByEmployee(@PathVariable("employee") String employee){
//        QAppointment appointment = new QAppointment("appointment");
//        BooleanExpression filter_employee = appointment.employees.contains(new Employee(, ""));
//
//        return (List<Appointment>) this.appointmentRepository.findAll(filter_employee);
//    }
}
