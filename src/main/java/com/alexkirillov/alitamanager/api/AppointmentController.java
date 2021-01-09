package com.alexkirillov.alitamanager.api;

import com.alexkirillov.alitamanager.api.helpers.AppointmentControllerHelper;
import com.alexkirillov.alitamanager.dao.appointmentrepo.AppointmentRepository;
import com.alexkirillov.alitamanager.models.Appointment;
import com.alexkirillov.alitamanager.models.QAppointment;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

@RestController
@CrossOrigin(origins = {"https://alita-manager-app.herokuapp.com",
        "http://localhost:7437", "http://127.0.0.1:7437", "https://127.0.0.1:7437"})
@RequestMapping(value = {"/api/schedule/appointments"})
public class AppointmentController {
    private AppointmentRepository appointmentRepository;
    private AppointmentControllerHelper helper = new AppointmentControllerHelper();

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository){
        this.appointmentRepository = appointmentRepository;
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
    public void updateAppointment(@RequestBody Appointment new_appointment){
        this.appointmentRepository.save(new_appointment);
    }

    @PutMapping(value = {"/add/one/", "/add/one"})
    public ResponseEntity insertAppointment(@RequestBody Appointment new_appointment){
        return helper.insertOneAppointment(new_appointment, this.appointmentRepository);
    }

//TODO NO FrontEnd implementation as of yet

//    @PutMapping(value = {"/add/many/", "/add/many"})
//    public void insertAppointmentList(@RequestBody List<Appointment> new_appointment_list){
//        this.appointmentRepository.insert(new_appointment_list);
//    }

    @DeleteMapping(value = {"/delete/id/{id}","/delete/id/{id}/"})
    @PreAuthorize("hasIpAddress('ROLE_ADMIN')")
    public void deleteAppointment(@PathVariable("id") String id){
        this.appointmentRepository.deleteById(id);
    }

    @DeleteMapping(value = {"/delete/all/day/id/{day_id}", "/delete/all/day/id/{day_id}/"})
    public void deleteAllAppointmentsOnWorkday(@PathVariable("day_id") String day_id){
        QAppointment appointment = new QAppointment("appointment");
        BooleanExpression filter = appointment.day_id.eq(day_id);
        List<Appointment> appointments_to_delete = (List<Appointment>) this.appointmentRepository.findAll(filter);
        this.appointmentRepository.deleteAll(appointments_to_delete);
    }

    //SERVER-SIDE MANIPULATIONS
    @DeleteMapping(value = "secret/{passKey}/dec/all")
    public void decrementDayIds(@PathVariable String passKey){

        if(passKey.equals(SECRET_KEY.getLoad())) {
            List<Appointment> appointments = this.appointmentRepository.findAll();
            this.appointmentRepository.deleteAll();
            appointments.stream()
                    .forEach(a -> a.setDay_id(Integer.parseInt(a.getDayId()) - 1));
            this.appointmentRepository.saveAll(appointments);
            return;
        }
        throw new AccessDeniedException("Denied access to this resource : key didn't match");
    }

    //TODO Search (NOT USED YET)
//
//    @GetMapping(value = {"/find/name/{name}","/find/name/{name}/"})
//    public List<Appointment> getByName(@PathVariable("name") String service_name){
//        QAppointment appointment = new QAppointment("appointment");
//        BooleanExpression filter_name = appointment.service.service_name.contains(service_name);
//
//        return (List<Appointment>) this.appointmentRepository.findAll(filter_name);
//    }
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
//        BooleanExpression filter_employee = appointment.employees.contains(employee);
//
//        return (List<Appointment>) this.appointmentRepository.findAll(filter_employee);
//    }
}
