package com.ericsson.c16.model;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class JobId implements Serializable {
    private String fullName;
    private long timestamp;

    public JobId() {
    }

    public JobId(String fullName, long timestamp) {
        this.fullName = fullName;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobId jobId = (JobId) o;
        return timestamp == jobId.timestamp && Objects.equals(fullName, jobId.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, timestamp);
    }
}
