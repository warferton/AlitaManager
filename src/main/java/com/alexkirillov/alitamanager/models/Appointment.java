package com.alexkirillov.alitamanager.models;

import com.alexkirillov.alitamanager.models.time.Interval;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;


//TODO Looks lIke total crap

@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;

    @NotBlank
    @Indexed(direction = IndexDirection.ASCENDING)
    private String day_id;

    @NotNull
    private Client client;

    @NotBlank
    private String date_time;

    @NotEmpty
    Service service;

    @NotBlank
    private Set<Employee> employees;

    private boolean confirmed;

    @NotNull
    private Interval interval;


    //TODO double data stored: {service} === {price, serviceName,estimatedTime}
    @JsonCreator
    public Appointment(@NotEmpty @JsonProperty("service") Service service
            , @NotEmpty @JsonProperty("date_time") String date_time
            , @NotNull @JsonProperty("client") Client client
            , @NotEmpty @NotNull @JsonProperty("employees")Set<Employee> employees
            , @NotEmpty @JsonProperty("confirmed") boolean confirmed) {
        this.date_time = date_time;
        this.day_id = parseDateToDayId(this.date_time);
        this.service = service;
        this.employees = employees;
        this.client = client;
        this.confirmed = confirmed;
        this.interval = new Interval(LocalDateTime.parse(
                this.date_time.substring(0, this.date_time.indexOf("."))),
                this.service.getEstimatedTime());
    }

    private String parseDateToDayId(String date_time){
        LocalDate date = LocalDate.parse(date_time.substring(0, date_time.indexOf('T')));
        String day_id = String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), date) + 1);
        return  day_id;
    }


    public Service getService(){
        return  this.service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public boolean isConfirmed() { return confirmed; }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    } //****becows cowme****//

    public Client getClient() {
        return client;
    }

    public String getDateTime(){
        return date_time;
    }

    public Interval getInterval() {
        return interval;
    }

    public String getId() {
        return id;
    }

    public String getDayId() {
        return day_id;
    }

    public void setDay_id(int day_id) {
        this.day_id = day_id + "";
    }

    public DBObject toDBObject(){
        return new BasicDBObject("service", service)
                .append("dayId", day_id)
                .append("employees", employees);
    }
}
