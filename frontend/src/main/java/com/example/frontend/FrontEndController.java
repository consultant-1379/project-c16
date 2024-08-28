package com.example.frontend;


import com.fasterxml.jackson.core.JsonToken;
import model.Job;
import model.RunsPerPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Controller
public class FrontEndController {

    @Autowired
    private RestTemplate restTemplate;


    @Value("${jobs.provider.url}")
    private String jobProviderURL;


    @RequestMapping(value="/", produces={"application/json"})
    public String getAllJobs(Model model){
        ResponseEntity<Collection<Job>> response =
                restTemplate.exchange(jobProviderURL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Collection<Job>>() {
                });
        ArrayList<Job> jobList = new ArrayList<>(Objects.requireNonNull(response.getBody()));
        model.addAttribute("jobList", jobList);
        return "home";
    }


    @RequestMapping(value="/averageDeliveryTime", produces={"application/json"})
    public String getAverageDeliveryTime(Model model){
        ResponseEntity<Collection<Map<String,Long>>> response =
                restTemplate.exchange(jobProviderURL+"/averageDeliveryTime",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        ArrayList<Map<String, Long>> listOfMaps = (ArrayList<Map<String, Long>>) response.getBody();
        Map<String,Long> averageDeliveryTimeMap = new HashMap<>();
        for(Map<String, Long> map: listOfMaps){
            for(Map.Entry<String,Long> entry : map.entrySet()){
                averageDeliveryTimeMap.put(entry.getKey(), TimeUnit.MILLISECONDS.toMinutes(entry.getValue()));
            }
        }
        model.addAttribute("averageDeliveryTimeMap", averageDeliveryTimeMap);
        return "averageDeliveryTime";
    }
    @RequestMapping(value="/runsPerPeriod", produces={"application/json"})
    public String getRunsPerPeriod(Model model){
        ResponseEntity<Collection<RunsPerPeriod>> response =
                restTemplate.exchange(jobProviderURL+"/runsPerPeriod",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        ArrayList<RunsPerPeriod> runsPerPeriodList = new ArrayList<>(Objects.requireNonNull(response.getBody()));
        model.addAttribute("runsPerPeriodList", runsPerPeriodList);
        return "runsPerPeriod";
    }

    @RequestMapping(value="/averageRestoreTime", produces={"application/json"})
    public String getAverageRestoreTime(Model model){
        ResponseEntity<Collection<Map<String,Long>>> response =
                restTemplate.exchange(jobProviderURL+"/averageRestoreTime",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        ArrayList<Map<String, Long>> listOfMaps = (ArrayList<Map<String, Long>>) response.getBody();
        Map<String,Long> averageRestoreTimeMap = new HashMap<>();
        for(Map<String, Long> map: listOfMaps){
            for(Map.Entry<String,Long> entry : map.entrySet()){
                averageRestoreTimeMap.put(entry.getKey(), TimeUnit.MILLISECONDS.toHours(entry.getValue()));
            }
        }
        model.addAttribute("averageRestoreTimeMap", averageRestoreTimeMap);
        return "averageRestoreTime";
    }
}
