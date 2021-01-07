package com.alexkirillov.alitamanager.dao.appointmentrepo;

import com.alexkirillov.alitamanager.models.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String>, QuerydslPredicateExecutor<Appointment> {
    Optional<Appointment> findById(String id);
}
