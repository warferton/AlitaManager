package com.alexkirillov.alitamanager.models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "employees")
public class Employee {
    @NotBlank
    private String first_name;
    @NotBlank @Indexed(direction = IndexDirection.ASCENDING)
    private String last_name;
    @Id
    private String id;

    public Employee(@NotBlank String first_name, @NotBlank String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getId(){
        return id;
    }

    private DBObject toDBObject(){
        return new BasicDBObject("firstName", first_name).append("lastName", last_name);
    }
}
