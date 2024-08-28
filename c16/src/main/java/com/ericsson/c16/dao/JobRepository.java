package com.ericsson.c16.dao;

import com.ericsson.c16.model.Job;
import com.ericsson.c16.model.JobId;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, JobId> {
}
