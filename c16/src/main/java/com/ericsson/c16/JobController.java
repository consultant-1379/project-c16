package com.ericsson.c16;

import com.ericsson.c16.dao.JobRepository;
import com.ericsson.c16.model.Job;
import com.ericsson.c16.model.PassFailRate;
import com.ericsson.c16.model.RunsPerPeriod;
import com.ericsson.c16.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/jobs")
@CrossOrigin
public class JobController {
    @Autowired
    private JobRepository repository;

    @Autowired
    private JobService service;

    @GetMapping(produces={"application/json"})
    public ResponseEntity<Collection<Job>> getAllJobs() {
        Collection<Job> result = (Collection<Job>) repository.findAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value="/averageDeliveryTime", produces={"application/json"})
    public ResponseEntity<Collection<Map<String, Long>>> averageDeliveryTime() {
        Collection<Map<String, Long>> result = service.averageDeliveryTime();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value="/runsPerPeriod", produces={"application/json"})
    public ResponseEntity<Collection<RunsPerPeriod>> runsPerPeriod() {
        Collection<RunsPerPeriod> result = service.getRunsPerPeriod();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value="/passFailRatePerPeriod", produces={"application/json"})
    public ResponseEntity<Collection<PassFailRate>> passFailRatePerPeriod() {
        Collection<PassFailRate> result = service.getPassFailRatePerPeriod();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value="/averageRestoreTime", produces={"application/json"})
    public ResponseEntity<Collection<Map<String, Long>>> averageRestoreTime() {
        Collection<Map<String, Long>> result = service.averageRestoreTime();
        return ResponseEntity.ok().body(result);
    }
}
