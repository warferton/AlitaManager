package com.alexkirillov.alitamanager.api;

import com.alexkirillov.alitamanager.dao.employeerepo.EmployeeRepository;
import com.alexkirillov.alitamanager.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://alita-manager-app.herokuapp.com")
@RequestMapping("/api/employees")
public class EmployeeController {
    private EmployeeRepository employeeRepository;
    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //CRUD
    @GetMapping(value = {"/",""})
    @PreAuthorize("hasAuthority('employees:read')")
    public List<Employee> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    @PostMapping(value = {"/update/", "/update"})
    @PreAuthorize("hasAuthority('employees:write')")
    public void updateEmployee(@RequestBody Employee new_employee){
        this.employeeRepository.save(new_employee);
    }

    @PutMapping(value ={"/add/one/", "/add/one"})
    @PreAuthorize("hasAuthority('employees:write')")
    public void insertEmployee(@RequestBody Employee new_employee){
        this.employeeRepository.insert(new_employee);
    }

    @PutMapping(value ={"/add/many/", "/add/many"})
    @PreAuthorize("hasAuthority('employees:write')")
    public void insertEmployeeList(@RequestBody List<Employee> new_employee){
        this.employeeRepository.insert(new_employee);
    }

    @DeleteMapping(value = {"/delete/id/{id}","/delete/id/{id}/"})
    @PreAuthorize("hasAuthority('employees:write')")
    public void deleteEmployee(@PathVariable("id") String id){
        this.employeeRepository.deleteById(id);
    }

    //TODO Search (NOT IMPLEMENTED)

//    @GetMapping(value = {"/id/{id}","/id/{id}/"})
//    @PreAuthorize("hasAuthority('employees:read')")
//    public Optional<Employee> getById(@PathVariable("id") String id){
//        Optional<Employee> employee = this.employeeRepository.findById(id);
//        return employee;
//    }
//
//    @GetMapping(value = {"/name/{name}","/name/{name}/"})
//    @PreAuthorize("hasAuthority('employees:read')")
//    public List<Employee> getByName(@PathVariable("name") String employee_name){
//        QEmployee qEmployee = new QEmployee("employee");
//        BooleanExpression filter_first_name = qEmployee.first_name.containsIgnoreCase(employee_name);
//        BooleanExpression filter_last_name = qEmployee.last_name.containsIgnoreCase(employee_name);
//
//        return (List<Employee>)this.employeeRepository.findAll(filter_first_name.or(filter_last_name));
//    }
}
