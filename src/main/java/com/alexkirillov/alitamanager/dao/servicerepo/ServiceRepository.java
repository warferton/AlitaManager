package com.alexkirillov.alitamanager.dao.servicerepo;

import com.alexkirillov.alitamanager.models.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends MongoRepository<Service, String>, QuerydslPredicateExecutor<Service> {
    Optional<Service> findById(String id);
}
