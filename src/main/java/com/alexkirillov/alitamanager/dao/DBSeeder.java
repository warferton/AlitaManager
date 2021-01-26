//package com.alexkirillov.alitamanager.dao;
//
//import com.alexkirillov.alitamanager.dao.appointmentrepo.AppointmentRepository;
//import com.alexkirillov.alitamanager.dao.clientrepo.ClientRepository;
//import com.alexkirillov.alitamanager.dao.employeerepo.EmployeeRepository;
//import com.alexkirillov.alitamanager.dao.workdayrepo.WorkdayRepository;
//import com.alexkirillov.alitamanager.dao.servicerepo.ServiceRepository;
//import com.alexkirillov.alitamanager.models.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.time.LocalDate;
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
//        Appointment ap_1 = new Appointment(service_1, LocalDate.now().plusDays(1).toString(),
//                client_1, Arrays.asList(employee_1), false);
//        Appointment ap_2 = new Appointment(service_1, LocalDate.now().plusDays(2).toString(),
//                client_1, Arrays.asList(employee_1), false);
//        Appointment ap_3 = new Appointment(service_1, LocalDate.now().plusDays(3).toString(),
//                client_2, Arrays.asList(employee_2), false);
//
//         this.appointmentRepository.deleteAll();
//
//
//        //populate the db
//
//        List<Appointment> appointments = Arrays.asList(ap_1, ap_2,ap_3);
//
//        this.appointmentRepository.saveAll(appointments);
//    }
//
//}
