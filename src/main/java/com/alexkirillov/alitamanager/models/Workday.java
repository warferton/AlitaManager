package com.alexkirillov.alitamanager.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Document(collection = "workdays")
public class Workday {
    @Id
    private String id;
    @NotNull
    @Indexed(direction = IndexDirection.ASCENDING)
    private String day_id;
    @NotNull
    private LocalDate date;

    @JsonCreator
    public Workday(@NotNull @JsonProperty("date") LocalDate date){
        this.date = date;
        this.day_id = String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), this.date) + 1);
    }

    public String getId(){
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDayId() {
        return day_id;
    }

    public void setDay_id(int day_id) {
        this.day_id = day_id + "";
    }

    @Override
    public String toString() {
        return "Workday{" +
                "date=" + date +
                ", dayId=" + day_id +
                '}';
    }


}
