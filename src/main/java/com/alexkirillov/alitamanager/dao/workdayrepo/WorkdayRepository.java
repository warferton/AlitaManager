package com.alexkirillov.alitamanager.dao.workdayrepo;

import com.alexkirillov.alitamanager.models.Workday;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WorkdayRepository extends MongoRepository<Workday, String>, QuerydslPredicateExecutor<Workday> {
    Optional<Workday> findById(String id);
}
