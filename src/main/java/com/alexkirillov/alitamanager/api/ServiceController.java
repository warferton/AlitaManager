package com.alexkirillov.alitamanager.api;

import com.alexkirillov.alitamanager.dao.servicerepo.ServiceRepository;
import com.alexkirillov.alitamanager.models.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://alita-manager-app.herokuapp.com")
@RequestMapping("/api/services")
public class ServiceController {
    private ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    //CRUD

    @GetMapping(value = {"", "/"})
    public List<Service> getAllServices(){
        List<Service> services = serviceRepository.findAll();
        return services;
    }

    @PostMapping(value = {"/update/", "/update"})
    @PreAuthorize("hasAuthority('services:write')")
    public void updateAppointment(@RequestBody Service new_service){
        this.serviceRepository.save(new_service);
    }

    @PutMapping(value = {"/add/one/", "/add/one"})
    @PreAuthorize("hasAuthority('services:write')")
    public void insertAppointment(@RequestBody Service new_service){
        this.serviceRepository.insert(new_service);
    }

    @DeleteMapping(value = {"/delete/id/{id}","/delete/id/{id}/"})
    @PreAuthorize("hasAuthority('services:write')")
    public void deleteService(@PathVariable("id") String service_id){
        this.serviceRepository.deleteById(service_id);
    }

    /**Should have severe protection*/
    @DeleteMapping(value = {"/delete/all","/delete/id/"})
    @PreAuthorize("hasAuthority('services:write')")
    public void deleteService(){
        serviceRepository.deleteAll();
    }

    //TODO Search (NOT IMPLEMENTED)

//    @GetMapping(value = {"/find/name/{name}","/find/name/{name}/"})
//    @PreAuthorize("hasAuthority('services:write')")
//    public List<Service> getByName(@PathVariable("name") String service_name){
//        QService service = new QService("service");
//        BooleanExpression filter_name = service.service_name.contains(service_name);
//
//        return (List<Service>) this.serviceRepository.findAll(filter_name);
//    }

}
