package com.alexkirillov.alitamanager.models;

import com.alexkirillov.alitamanager.models.time.Interval;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "employees")
public class Employee {
    @NotBlank
    private String first_name;
    @NotBlank @Indexed(direction = IndexDirection.ASCENDING)
    private String last_name;
    @NotBlank
    private Duration workday_time;

    private Map<Integer ,List<Interval>> intervals = new HashMap<>();
    @Id
    private String id;

    public Employee(@NotBlank String first_name, @NotBlank String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.workday_time = Duration.of(11, ChronoUnit.HOURS);
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

    public Duration getWorkdayTime() {
        return workday_time;
    }

    public void setWorkdayTime(Duration workday_time) {
        this.workday_time = workday_time;
    }

    /**
     * Be sure to adjust fot day index, (starts at 0)
     * @return - a list of a list of all
     * intervals that are assigned
     * for this employee
     * */
    public List<List<Interval>> getIntervals() {
        List<List<Interval>> return_list = new ArrayList<>();
        for(Map.Entry<Integer, List<Interval>> entry : intervals.entrySet()){
            return_list.add(entry.getValue());
        }
        return return_list;
    }

    /**
     * Be sure to adjust fot day index, (starts at 0)
     * @return - a list of a list of intervals that
     * are assigned on a specified day
     * for this employee
     * */
    public List<Interval> getIntervals(int day_index) {
        List<List<Interval>> return_list = new ArrayList<>();
        for(Map.Entry<Integer, List<Interval>> entry : intervals.entrySet()){
            return_list.add(entry.getValue());
        }
        return return_list.get(day_index);
    }

    /**
     * Be sure to adjust fot day index, (starts at 0)
     * @return true - if the interval is added <br/>
     * false - if there is overlap
     * */
    public boolean addInterval(Interval interval, int day_index) {

        List<Interval> intervals_on_day = this.intervals.get(day_index);

        if (intervals_on_day == null) {
            if (day_index <= 30 && day_index > 0){
                this.intervals.put(day_index, Arrays.asList(new Interval[]{interval}));
                return true;
            }
            return false;
        }

        boolean overlap = intervals_on_day.stream().anyMatch(i -> i.isOverlapping(interval));

        if (!overlap) {
            List<Interval> updated_list = this.intervals.get(day_index);
            updated_list.add(interval);
            if (day_index <= 30 && day_index > 0){
                this.intervals.put(day_index, updated_list);
                return true;
            }
        }
        return false;
    }

    public Duration decreaseTime(Duration decrease_time){
        if(workday_time.compareTo(decrease_time) <= 0){
            throw new IllegalArgumentException("Illegal Argument encountered. " +
                    "(The appointment duration is longer or the same to the work time left)");
        }
        this.setWorkdayTime(this.workday_time.minus(decrease_time));
        return this.workday_time;

    }

    /**
     * Shifts every value in the Intervals map a key higher, and
     *
     * */
    public void shiftDays(){
        int size = this.intervals.size();

        //shift map values 1 key up
        for(int i = 0; i < size; i++){
            try {
                this.intervals.put(i, this.intervals.get(i + 1));
                this.intervals.remove(i + 1);
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }

    }

    public String getId(){
        return id;
    }

}
