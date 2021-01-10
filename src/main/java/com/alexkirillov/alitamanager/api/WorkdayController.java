package com.alexkirillov.alitamanager.api;

import com.alexkirillov.alitamanager.dao.workdayrepo.WorkdayRepository;
import com.alexkirillov.alitamanager.models.QWorkday;
import com.alexkirillov.alitamanager.models.Workday;
import com.alexkirillov.alitamanager.models.time.Interval;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

@RestController
@CrossOrigin(origins = {"https://alita-manager-app.herokuapp.com",
        "https://fast-sierra-37663.herokuapp.com"})
@RequestMapping("/api/schedule/workdays")
public class WorkdayController {
    private final WorkdayRepository workdayRepository;

    @Autowired
    public WorkdayController(WorkdayRepository workdayRepository) {
        this.workdayRepository = workdayRepository;
    }

    //CRUD
    @GetMapping(value = {"/",""})
    @PreAuthorize("hasAuthority('workdays:read')")
    public List<Workday> getAllWorkdays(){
        List<Workday> workdays = this.workdayRepository.findAll();
        return workdays;
    }

    @PostMapping(value = {"/update/one/", "/update/one"})
    @PreAuthorize("hasAuthority('workdays:write')")
    public void updateWorkday(@RequestBody Workday new_workday){
        this.workdayRepository.save(new_workday);
    }

    //TODO potential backfire??????
    @PutMapping(value = {"/add/one", "/add/one/"})
    public void insertWorkday(@RequestBody Workday new_workday){
        int wd_num = this.workdayRepository.findAll().size();
        if ((wd_num < 31)) {
            this.workdayRepository.insert(new_workday);
        } else {
            System.out.println("Workday number exceeded");
        }
    }

    @DeleteMapping(value = {"/delete/dayId/{day_id}", "/delete/dayId/{day_id}/"})
    public void deleteWorkday(@PathVariable("day_id") String day_id){
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder.build();
        String delete_url = "http://localhost:8080/api/schedule/appointments/delete/all/day/id/{day_id}";
        try {

            restTemplate.delete(delete_url, day_id);

        }catch (HttpServerErrorException httpException) {
            System.err.println("HTTPServerErrorException. CODE: 500.  No appointments, collection is empty");
        }

        String id = this.getByDayId(day_id).get(0).getDayId();
        this.workdayRepository.deleteById(id);
        System.out.println("WD_ID: " + id + "  --DELETED");
    }

    //Search
    @GetMapping(value = {"/find/byDate/{date}","/find/byDate/{date}/"})
    @PreAuthorize("hasAuthority('workdays:read')")
    public List<Workday> getByDate(@PathVariable String date){
        QWorkday workday = new QWorkday("workday");
        BooleanExpression filter = workday.date.eq(LocalDate.parse(date));//format: yyyy-mm-dd
        return (List<Workday>) this.workdayRepository.findAll(filter);
    }

    @GetMapping(value = {"/find/byDayId/{day_id}","/find/byDayId/{day_id}/"})
    public List<Workday> getByDayId(@PathVariable String day_id){
        QWorkday workday = new QWorkday("workday");
        BooleanExpression filter = workday.day_id.eq(day_id);
        return (List<Workday>) this.workdayRepository.findAll(filter);
    }

    //SERVER-SIDE MANIPULATIONS

    //DECR all DayIds and delete the first one
    @DeleteMapping(value = {"/secret/{passKey}/dec/all"})
    public ResponseEntity<String> decrementDayIds(@PathVariable String passKey){
        if(passKey.equals(SECRET_KEY.getLoad())) {
            List<Workday> workdays = this.workdayRepository.findAll();
            this.workdayRepository.deleteAll();
            if(Integer.parseInt(workdays.get(0).getDayId()) <= 0)
                workdays.remove(0);
            workdays
                    .forEach(w -> w.setDay_id(Integer.parseInt(w.getDayId()) - 1));
            this.workdayRepository.saveAll(workdays);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PostMapping(value = "/secret/{passKey}/time/adjust/dayId/{dayId}")
    public ResponseEntity<String> adjustWorkTime(@PathVariable String passKey,
                               @PathVariable String dayId,
                               @RequestBody Interval interval){
        if(passKey.equals(SECRET_KEY.getLoad())){
            try {
                Workday workday = this.getByDayId(dayId).get(0);

                //throws
                workday.decreaseTime(interval.getDuration());

                //insert time range;
               boolean interval_flag =  workday.addInterval(interval);
               if(interval_flag){
                //update the workday in db
                    this.workdayRepository.save(workday);
                    return new ResponseEntity<>(HttpStatus.OK);
               }

            }catch (Exception e){
                e.printStackTrace();
            }
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }




}
