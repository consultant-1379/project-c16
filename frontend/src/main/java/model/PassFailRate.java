package model;

import java.util.HashMap;

public class PassFailRate {
    private String jobname;
    private HashMap<String, Integer> perDay;
    private HashMap<String, Integer> perWeek;
    private HashMap<String, Integer> perMonth;
    private HashMap<String, Integer> perPerYear;

    public PassFailRate(String jobname) {
        this.jobname = jobname;
        this.perDay = new HashMap<String, Integer>();
        this.perDay.put("SUCCESS", 0);
        this.perDay.put("FAILURE", 0);
        this.perWeek = new HashMap<String, Integer>();
        this.perWeek.put("SUCCESS", 0);
        this.perWeek.put("FAILURE", 0);
        this.perMonth = new HashMap<String, Integer>();
        this.perMonth.put("SUCCESS", 0);
        this.perMonth.put("FAILURE", 0);
        this.perPerYear = new HashMap<String, Integer>();
        this.perPerYear.put("SUCCESS", 0);
        this.perPerYear.put("FAILURE", 0);
    }

    public PassFailRate(String jobname, HashMap<String, Integer> perDay, HashMap<String, Integer> perWeek, HashMap<String, Integer> perMonth, HashMap<String, Integer> perPerYear) {
        this.jobname = jobname;
        this.perDay = perDay;
        this.perWeek = perWeek;
        this.perMonth = perMonth;
        this.perPerYear = perPerYear;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public HashMap<String, Integer> getPerDay() {
        return perDay;
    }

    public void setPerDay(HashMap<String, Integer> perDay) {
        this.perDay = perDay;
    }

    public HashMap<String, Integer> getPerWeek() {
        return perWeek;
    }

    public void setPerWeek(HashMap<String, Integer> perWeek) {
        this.perWeek = perWeek;
    }

    public HashMap<String, Integer> getPerMonth() {
        return perMonth;
    }

    public void setPerMonth(HashMap<String, Integer> perMonth) {
        this.perMonth = perMonth;
    }

    public HashMap<String, Integer> getPerPerYear() {
        return perPerYear;
    }

    public void setPerPerYear(HashMap<String, Integer> perPerYear) {
        this.perPerYear = perPerYear;
    }
}
