package com.alexkirillov.alitamanager.models.time;


import java.time.Duration;
import java.time.LocalDateTime;

public class Interval {

   private LocalDateTime startTime;

   private Duration duration;

   private LocalDateTime endTime;


    public Interval(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration(){
        return Duration.between(this.startTime, this.endTime);
    }

    public boolean isOverlapping(Interval other){
        return !this.startTime.isAfter(other.endTime) && !this.endTime.isBefore(other.startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interval)) return false;
        Interval interval = (Interval) o;
        return getStartTime().equals(interval.getStartTime()) &&
                getEndTime().equals(interval.getEndTime());
    }

    @Override
    public String toString() {
        return "Interval{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
