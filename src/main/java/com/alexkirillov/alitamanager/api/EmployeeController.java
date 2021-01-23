package com.alexkirillov.alitamanager.api;

import com.alexkirillov.alitamanager.dao.employeerepo.EmployeeRepository;
import com.alexkirillov.alitamanager.models.Employee;
import com.alexkirillov.alitamanager.models.QEmployee;
import com.alexkirillov.alitamanager.models.time.Interval;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

@RestController
@CrossOrigin(origins = {"https://alita-manager-app.herokuapp.com"})
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

    @GetMapping(value = {"/public/names","/public/names/"})
    public List<String> getAllEmployeesFirstNames(){
        List<Employee> employees = employeeRepository.findAll();
        List<String> employee_first_names = new ArrayList<>();
        employees.forEach(e -> employee_first_names.add(e.getFirstName()));
        return employee_first_names;
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

    @GetMapping(value = {"/id/{id}","/id/{id}/"})
    @PreAuthorize("hasAuthority('employees:read')")
    public Optional<Employee> getById(@PathVariable("id") String id){
        Optional<Employee> employee = this.employeeRepository.findById(id);
        return employee;
    }

    //SERVER-SIDE MANIPULATIONS

    public ResponseEntity<String> adjustWorkTime(@PathVariable String passKey,
                                                 @PathVariable String employee_id,
                                                 @RequestBody Interval interval,
                                                 @RequestBody int day_index){
        if(passKey.equals(SECRET_KEY.getLoad())){
            try {
                Employee employee = this.getById(employee_id).get();

                //throws
                employee.decreaseTime(interval.getDuration());

                //insert time range;
                boolean interval_flag =  employee.addInterval(interval, day_index);
                if(interval_flag){
                    //update the workday in db

                    this.employeeRepository.save(employee);
                    return new ResponseEntity<>(HttpStatus.OK);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * Retrieves all employees from the db into a list
     * and shifts their Interval Maps a key back.
     * Then puts the updated data back to the db
     * @return Status Code 200
     * */
    public ResponseEntity<String> shiftSchedules(){
        List<Employee> employees = this.getAllEmployees();

        employees.forEach(Employee::shiftDays);

        this.employeeRepository.saveAll(employees);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    //TODO Search (NOT IMPLEMENTED)

    @GetMapping(value = {"/name/{name}","/name/{name}/"})
    public List<Employee> getByName(@PathVariable("name") String employee_name){
        QEmployee qEmployee = new QEmployee("employee");
        BooleanExpression filter_first_name = qEmployee.first_name.containsIgnoreCase(employee_name);
        BooleanExpression filter_last_name = qEmployee.last_name.containsIgnoreCase(employee_name);

        return (List<Employee>)this.employeeRepository.findAll(filter_first_name.or(filter_last_name));
    }
}
