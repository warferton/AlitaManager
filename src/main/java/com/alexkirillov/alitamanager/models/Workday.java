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
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "workdays")
public class Workday {
    @Id
    private String id;
    @NotNull
    @Indexed(direction = IndexDirection.ASCENDING)
    private String day_id;
    @NotNull
    private LocalDate date;
    @NotBlank
    private Duration workday_time;

    private List<Interval> intervals = new ArrayList<>();

    @JsonCreator
    public Workday(@NotNull @JsonProperty("date") LocalDate date){
        this.date = date;
        this.day_id = String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), this.date) + 1);
        workday_time = Duration.of(14, ChronoUnit.HOURS);
    }

    public String getId(){
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Duration getWorkdayTime() {
        return workday_time;
    }

    public void setWorkdayTime(Duration workday_time) {
        this.workday_time = workday_time;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }
    //TODO TESTING
    public boolean addInterval(Interval interval) {
            boolean overlap = this.intervals.stream().anyMatch(i -> i.isOverlapping(interval));

            if (!overlap) {
                return this.intervals.add(interval);
            }
            return false;
    }

    public Duration decreaseTime(int val, ChronoUnit time_unit){
        Duration app_duration = Duration.of(val, time_unit);
        if(workday_time.compareTo(app_duration) <= 0){
            throw new IllegalArgumentException("Illegal Argument encountered. " +
                    "(The appointment duration is longer or the same to the work time left)");
        }
        this.setWorkdayTime(this.workday_time.minus(val, time_unit));
        return this.workday_time;
    }

    public Duration decreaseTime(Duration decrease_time){
        if(workday_time.compareTo(decrease_time) <= 0){
            throw new IllegalArgumentException("Illegal Argument encountered. " +
                    "(The appointment duration is longer or the same to the work time left)");
        }
        this.setWorkdayTime(this.workday_time.minus(decrease_time));
        return this.workday_time;

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
                ", workdayTime=" + workday_time +
                ", dayId=" + day_id +
                '}';
    }

    public DBObject toDBObject(){
        return new BasicDBObject("date",date).append("day",day_id)
                .append("workdayTime", workday_time);
    }
}
