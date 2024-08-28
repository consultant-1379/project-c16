package com.ericsson.c16.service;

import com.ericsson.c16.model.Job;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class JenkinsService {
    private Client client;

    public Client getClient() {
        if (this.client != null) {
            return client;
        } else {
            try {
                Client client = Client.create();
                client.addFilter(new HTTPBasicAuthFilter("EWOJDAM", "119bdd76c184ba0fd046681cc26f1b6058"));
                this.client = client;
                return client;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getWebResource(String resourceURL) throws RuntimeException {
        Client client = getClient();

        WebResource webResource = client
                .resource(resourceURL);

        ClientResponse response = webResource.type("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);
        return output;
    }

    public List<Job> getBuildHistory() {
        ArrayList<Job> buildJobList = new ArrayList<>();

        String resourceURL = "https://fem1s11-eiffel216.eiffel.gic.ericsson.se:8443/jenkins/api/json?tree=jobs" +
        "[name,builds[id,result,duration,timestamp]%7B0,50%7D]";

        String buildHistoryStr = getWebResource(resourceURL);

        JSONObject jsonObject = new JSONObject(buildHistoryStr);
        JSONArray jobs = new JSONArray(jsonObject.getJSONArray("jobs"));

        // Iterate over the jobs
        for (int jobIndex = 0; jobIndex < jobs.length(); jobIndex++) {
            JSONObject job = jobs.getJSONObject(jobIndex);
            String jobName = job.getString("name");
            JSONArray builds = job.getJSONArray("builds");

            // Iterate over the builds in the job
            for (int buildIndex = 0; buildIndex < builds.length(); buildIndex++) {
                try {
                    // Get attributes and data
                    JSONObject build = builds.getJSONObject(buildIndex);
                    Long duration = build.getLong("duration");
                    String result = build.getString("result");
                    Long id = build.getLong("id");
                    Long timestamp = build.getLong("timestamp");

                    Job newJob = new Job(id, timestamp, jobName, duration, result);
                    buildJobList.add(newJob);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return buildJobList;
    }
}
