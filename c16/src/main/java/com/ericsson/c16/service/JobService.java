package com.ericsson.c16.service;

import com.ericsson.c16.dao.JobRepository;
import com.ericsson.c16.model.Job;
import com.ericsson.c16.model.JobId;
import com.ericsson.c16.model.PassFailRate;
import com.ericsson.c16.model.RunsPerPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JobService {
	@Autowired
	private JobRepository repository;

	@Autowired
	private JenkinsService jenkinsService;

	public void updateDatabaseBuildHistory() {
		List<Job> buildHistory = jenkinsService.getBuildHistory();
		System.out.println(buildHistory);
		repository.saveAll(buildHistory);
	}

	public HashMap<String, List<Job>> getJobsByNameMap() {
		HashMap<String, List<Job>> jobByNameMap = new HashMap<String, List<Job>>();
		ArrayList<Job> allBuilds = (ArrayList<Job>) repository.findAll();
		for (Job job : allBuilds) {
			String fullName = job.getFullName();
			if (!jobByNameMap.containsKey(fullName)) {
				jobByNameMap.put(fullName, new ArrayList<>());
			}
			jobByNameMap.get(fullName).add(job);
		}

		return jobByNameMap;
	}

	public List<Map<String, Long>> averageDeliveryTime() {
		List<Map<String, Long>> averageDeliveryTimes = new ArrayList<>();
		HashMap<String, List<Job>> jobByNameMap = getJobsByNameMap();

		for (String jobName : jobByNameMap.keySet()) {
			long totalDuration = 0;
			int numberOfDurations = 0;
			List<Job> builds = jobByNameMap.get(jobName);

			for (Job build : builds) {
				long duration = build.getDuration();
				if (duration != 0) {
					totalDuration += duration;
					numberOfDurations += 1;
				}
			}
			HashMap<String, Long> averageDeliveryTimeMap = new HashMap<>();
			Long averageDelivery = -1L;
			if (numberOfDurations != 0) {
				averageDelivery = totalDuration / numberOfDurations;
			}
			averageDeliveryTimeMap.put(jobName, averageDelivery);
			averageDeliveryTimes.add(averageDeliveryTimeMap);
		}

		return averageDeliveryTimes;
	}

	public List<RunsPerPeriod> getRunsPerPeriod() {
		HashMap<String, List<Job>> jobByNameMap = getJobsByNameMap();
		List<RunsPerPeriod> runsPerPeriodList = new ArrayList<>();

		// Get the number of runs
		for (String jobName : jobByNameMap.keySet()) {
			List<Job> builds = jobByNameMap.get(jobName);
			int numberOfBuilds = builds.size();

			if (numberOfBuilds == 0) {
				RunsPerPeriod runsPerPeriod = new RunsPerPeriod(jobName, null, null, null, null);
				runsPerPeriodList.add(runsPerPeriod);
				continue;
			}

			double runsPastDay = 0;
			double runsPastWeek = 0;
			double runsPastMonth = 0;
			double runsPastYear = 0;
			for (Job build : builds) {
				long timestamp = build.getTimestamp();
				Date timeStampDate = new java.util.Date(timestamp);
				System.out.println("## TIMESTAMP DATE: " + timeStampDate);

				// Get the number of days between first run and current date
				Date now = getCurrentTime();
				double timeDiffMs = now.getTime() - (double)timeStampDate.getTime();
				double timeDiffSec = timeDiffMs / 1000.0;
				double timeDiffDays = timeDiffSec / 86400.0;

//				System.out.println(timeDiffSec + " | timeDiffDays: " + timeDiffDays + " " + (timeDiffDays <= 1.0));

				if (timeDiffDays <= 1.0) { runsPastDay += 1; }
				if (timeDiffDays <= 7.0) { runsPastWeek += 1; }
				if (timeDiffDays <= 28.0) { runsPastMonth += 1; }
				if (timeDiffDays <= 365.0) { runsPastYear += 1; }
			}
			RunsPerPeriod runsPerPeriod = new RunsPerPeriod(jobName, runsPastDay, runsPastWeek, runsPastMonth, runsPastYear);
			runsPerPeriodList.add(runsPerPeriod);
		}
		return runsPerPeriodList;
	}

	public Date getCurrentTime() {
		return java.util.Calendar.getInstance().getTime();
	}

	public List<PassFailRate> getPassFailRatePerPeriod() {
		HashMap<String, List<Job>> jobByNameMap = getJobsByNameMap();
		List<PassFailRate> passFailPeriodList = new ArrayList<>();

		// Get the number of runs
		for (String jobName : jobByNameMap.keySet()) {
			List<Job> builds = jobByNameMap.get(jobName);

			PassFailRate passFailRate = new PassFailRate(jobName);
			for (Job build : builds) {
				String result = build.getResult();
				long timestamp = build.getTimestamp();
				Date timeStampDate = new java.util.Date(timestamp);

				// Get the number of days between first run and current date
				Date now = getCurrentTime();
				double timeDiffMs = now.getTime() - (double)timeStampDate.getTime();
				double timeDiffSec = timeDiffMs / 1000;
				double timeDiffDays = timeDiffSec / 86400;

				System.out.println(timeDiffDays);

				if (timeDiffDays <= 1) {
					HashMap<String, Integer> pastDay = passFailRate.getPerDay();
					if (pastDay.containsKey(result)) {
						Integer resultCount = pastDay.get(result);
						pastDay.put(result, resultCount + 1);
						passFailRate.setPerDay(pastDay);
					}
				}
				if (timeDiffDays <= 7) {
					HashMap<String, Integer> pastWeek = passFailRate.getPerWeek();
					if (pastWeek.containsKey(result)) {
						Integer resultCount = pastWeek.get(result);
						pastWeek.put(result, resultCount + 1);
						passFailRate.setPerWeek(pastWeek);
					}
				}
				if (timeDiffDays <= 28) {
					HashMap<String, Integer> pastMonth = passFailRate.getPerMonth();
					if (pastMonth.containsKey(result)) {
						Integer resultCount = pastMonth.get(result);
						pastMonth.put(result, resultCount + 1);
						passFailRate.setPerMonth(pastMonth);
					}
				}
				if (timeDiffDays <= 365) {
					HashMap<String, Integer> pastYear = passFailRate.getPerPerYear();
					if (pastYear.containsKey(result)) {
						Integer resultCount = pastYear.get(result);
						pastYear.put(result, resultCount + 1);
						passFailRate.setPerPerYear(pastYear);
					}
				}
			}

			passFailPeriodList.add(passFailRate);
		}
		return passFailPeriodList;
	}

	public List<Map<String, Long>> averageRestoreTime() {
		List<Map<String, Long>> averageRestoreTimes = new ArrayList<>();
		HashMap<String, List<Job>> jobByNameMap = getJobsByNameMap();

		long totalRestoreTime = 0;
		int numberOfRestores = 0;
		// Iterate over jobs
		for (String jobName : jobByNameMap.keySet()) {
			List<Job> builds = jobByNameMap.get(jobName);

			Job failedBuild = null;
			// Iterate over builds
			for (Job build : builds) {
				// Iterate until a failure is found
				if ("FAILURE".equals(build.getResult())) {
					failedBuild = build;
					continue;
				}
				// If a failure is found, look for next success
				if (failedBuild != null) {
					if ("SUCCESS".equals(build.getResult())) {
						// Get difference between failure and success
						totalRestoreTime += build.getTimestamp() - failedBuild.getTimestamp();
						numberOfRestores += 1;
						// Clear the failed variable
						failedBuild = null;
					}
				}
			}

			// Calculate average
			HashMap<String, Long> averageRestoreTimeMap = new HashMap<>();
			Long averageRestore = -1L;
			if (numberOfRestores > 0) {
				averageRestore = totalRestoreTime / numberOfRestores;
			}
			averageRestoreTimeMap.put(jobName, averageRestore);
			averageRestoreTimes.add(averageRestoreTimeMap);
		}

		return averageRestoreTimes;
	}
}
