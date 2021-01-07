package com.alexkirillov.alitamanager.dao.employeerepo;

import com.alexkirillov.alitamanager.models.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Employees")
public interface EmployeeRepository extends MongoRepository<Employee, String>, QuerydslPredicateExecutor<Employee> {
    Optional<Employee> findById(String id);
}
