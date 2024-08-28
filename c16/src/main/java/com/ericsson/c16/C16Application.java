package com.ericsson.c16;

import com.ericsson.c16.service.JobService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class C16Application {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(C16Application.class, args);

		JobService jobService = context.getBean(JobService.class);
//		jobService.doDemo();
		jobService.updateDatabaseBuildHistory();
	}
}
