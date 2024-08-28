package com.ericsson.scheduler;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SchedulerApplication {

	public static void main(String[] args) throws URISyntaxException {
		ApplicationContext context = SpringApplication.run(SchedulerApplication.class, args);

		URI jenkinsURI = new URI("https://fem1s11-eiffel216.eiffel.gic.ericsson.se:8443/jenkins");

		JenkinsServer jenkins = new JenkinsServer(jenkinsURI, "EWOJDAM", "119bdd76c184ba0fd046681cc26f1b6058");



		try {
			int count = 1;
			Map<String, Job> jobs = jenkins.getJobs();
			for (String k : jobs.keySet()) {
				Job job = jobs.get(k);
				JobWithDetails jobWithDetails = job.details();
				Build lastSuccessfulBuild = jobWithDetails.getLastSuccessfulBuild();
				Build lastFailedBuild = jobWithDetails.getLastFailedBuild();
				BuildWithDetails buildWithDetails = lastSuccessfulBuild.details();

				// Job Details
				String fullName = jobWithDetails.getFullName();

				// Build Details
				Long timestamp = buildWithDetails.getTimestamp();
				Long duration = buildWithDetails.getDuration();
				String id = buildWithDetails.getId();
				BuildResult result = buildWithDetails.getResult();

				System.out.println("\n" + count);
				System.out.println("fullName: " + fullName + "\n"
						+ "timestamp: " + timestamp + "\n"
						+ "duration: " + duration + "\n"
						+ "id: " + id + "\n"
						+ "result: " + result + "\n");
				count += 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
