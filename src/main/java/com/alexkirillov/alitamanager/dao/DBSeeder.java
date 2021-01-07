//package com.alexkirillov.alitabot.dao;
//
//import com.alexkirillov.alitabot.dao.appointmentrepo.AppointmentRepository;
//import com.alexkirillov.alitabot.dao.clientrepo.ClientRepository;
//import com.alexkirillov.alitabot.dao.employeerepo.EmployeeRepository;
//import com.alexkirillov.alitabot.dao.workdayrepo.WorkdayRepository;
//import com.alexkirillov.alitabot.dao.servicerepo.ServiceRepository;
//import com.alexkirillov.alitabot.models.*;
//import org.apache.tomcat.jni.Local;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.time.temporal.ChronoUnit;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class DBSeeder implements CommandLineRunner {
//    private EmployeeRepository employeeRepository;
//    private ClientRepository clientRepository;
//    private WorkdayRepository workdayRepository;
//    private AppointmentRepository appointmentRepository;
//    private ServiceRepository serviceRepository;
//
//    @Autowired
//    public DBSeeder(EmployeeRepository employeeRepository, ClientRepository clientRepository
//            , WorkdayRepository workdayRepository, AppointmentRepository appointmentRepository
//            , ServiceRepository serviceRepository) {
//        this.employeeRepository = employeeRepository;
//        this.clientRepository = clientRepository;
//        this.workdayRepository = workdayRepository;
//        this.appointmentRepository = appointmentRepository;
//        this.serviceRepository = serviceRepository;
//
//    }
//
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        //Services
//        Service service_1 = new Service("Haircut(man)",
//                Duration.of(35, ChronoUnit.MINUTES),400);
//        Service service_2 = new Service("Drying",
//                Duration.of(15, ChronoUnit.MINUTES),200);
//        Service service_3 = new Service("Nails",
//                Duration.of(75, ChronoUnit.MINUTES),1300);
//        Service service_4 = new Service("tester",
//                Duration.of(40, ChronoUnit.HOURS),1300);
//
//
//
//        //employees
//        Employee employee_1 = new Employee("Mika", "Landemark");
//        Employee employee_2 = new Employee("Sarah", "Loighman");
//
//        //clients
//        Client client_1 = new Client("Zolan", "Leikman", "891247233833");
//        Client client_2 = new Client("Nolan", "Steinberg",  "93742341342");
//
//
//        //empty db
//        this.clientRepository.deleteAll();
//        this.employeeRepository.deleteAll();
//        this.workdayRepository.deleteAll();
//        this.appointmentRepository.deleteAll();
//        this.serviceRepository.deleteAll();
//
//
//        //populate the db
//        List<Client> clients = Arrays.asList(client_1,client_2);
//        List<Employee> employees = Arrays.asList(employee_1,employee_2);
//        List<Service> services = Arrays.asList(service_1, service_2, service_3, service_4);
//
//        this.serviceRepository.saveAll(services);
//        this.employeeRepository.saveAll(employees);
//        this.clientRepository.saveAll(clients);
//    }
//
//}
