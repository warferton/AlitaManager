package com.alexkirillov.alitamanager.dao.clientrepo;

import com.alexkirillov.alitamanager.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Clients")
public interface ClientRepository extends MongoRepository<Client, String>, QuerydslPredicateExecutor<Client> {
    Optional<Client> findById(String id);
}
