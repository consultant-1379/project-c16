package model;

public class RunsPerPeriod {
    private String jobName;
    private Double perDay;
    private Double perWeek;
    private Double perMonth;
    private Double perPerYear;

    public RunsPerPeriod() {
    }

    public RunsPerPeriod(String jobName, Double perDay, Double perWeek, Double perMonth, Double perPerYear) {
        this.jobName = jobName;
        this.perDay = perDay;
        this.perWeek = perWeek;
        this.perMonth = perMonth;
        this.perPerYear = perPerYear;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Double getPerDay() {
        return perDay;
    }

    public void setPerDay(Double perDay) {
        this.perDay = perDay;
    }

    public Double getPerWeek() {
        return perWeek;
    }

    public void setPerWeek(Double perWeek) {
        this.perWeek = perWeek;
    }

    public Double getPerMonth() {
        return perMonth;
    }

    public void setPerMonth(Double perMonth) {
        this.perMonth = perMonth;
    }

    public Double getPerPerYear() {
        return perPerYear;
    }

    public void setPerPerYear(Double perPerYear) {
        this.perPerYear = perPerYear;
    }
}
