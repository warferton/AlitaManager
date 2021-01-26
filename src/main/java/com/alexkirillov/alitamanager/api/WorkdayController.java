package com.alexkirillov.alitamanager.api;

import com.alexkirillov.alitamanager.dao.workdayrepo.WorkdayRepository;
import com.alexkirillov.alitamanager.models.QWorkday;
import com.alexkirillov.alitamanager.models.Workday;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.alexkirillov.alitamanager.security.pathwaykeys.PathKeys.SECRET_KEY;

@RestController
@CrossOrigin(origins = {"https://alita-manager-app.herokuapp.com"})
@RequestMapping("/api/schedule/workdays")
public class WorkdayController {
    private WorkdayRepository workdayRepository;

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
    public ResponseEntity<String> deleteWorkday(@PathVariable("day_id") String day_id) {
        try{

            String id = this.getByDayId(day_id).get(0).getId();
            this.workdayRepository.deleteById(id);
            System.out.println("WD_ID: " + id + "  --DELETED");
            return new ResponseEntity(HttpStatus.OK);

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return new ResponseEntity<>(HttpStatus.valueOf(400));
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

    //DECR all DayIds and delete all the workdays that are before today
    @DeleteMapping(value = {"/secret/{passKey}/dec/all"})
    public ResponseEntity<String> decrementDayIds(@PathVariable String passKey){
        if(passKey.equals(SECRET_KEY.getLoad())) {
            List<Workday> workdays = this.workdayRepository.findAll();

            //delete all days and appointments before today
            workdays.forEach(w -> {
                    if(w.getDate().isBefore(LocalDate.now())){
                        this.deleteWorkday(w.getDayId());
                    }
            });

            //erase all deleted workdays
            workdays = this.workdayRepository.findAll();
            //clean the DB
            this.workdayRepository.deleteAll();

            if(!workdays.isEmpty()) {
                //extra check so that the first day in the list don't have the dayId = 0
                if (Integer.parseInt(workdays.get(0).getDayId()) <= 0)
                    workdays.remove(0);

                //DECR the indexes
                if(Integer.parseInt(workdays.get(0).getDayId()) > 1)
                    workdays
                            .forEach(w -> w.setDay_id(Integer.parseInt(w.getDayId()) - 1));
            }
            //put workdays back to the DB
            this.workdayRepository.saveAll(workdays);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }




}
