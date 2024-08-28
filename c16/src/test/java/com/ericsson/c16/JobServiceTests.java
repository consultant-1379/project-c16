package com.ericsson.c16;

import com.ericsson.c16.dao.JobRepository;
import com.ericsson.c16.model.Job;
import com.ericsson.c16.model.PassFailRate;
import com.ericsson.c16.model.RunsPerPeriod;
import com.ericsson.c16.service.JenkinsService;
import com.ericsson.c16.service.JobService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest
@ConfigurationProperties(prefix = "test")
class JobServiceTests {
    ArrayList<Job> mockJobs;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @BeforeEach
    public void setup() {
        mockJobs = new ArrayList<>();
        Job mockJob1 = new Job(1, 1629135809077L, "TestJob", 1000L, "SUCCESS");
        Job mockJob2 = new Job(2, 1634897300000L, "TestJob", 2000L, "SUCCESS");
        Job mockJob3 = new Job(3, 1634595346000L, "TestJob3", 2000L, "FAILURE");
        mockJobs.add(mockJob1);
        mockJobs.add(mockJob2);
        mockJobs.add(mockJob3);
    }

    @Test
    public void testGetJobsByNameMapReturnStructure(){
        when(jobRepository.findAll()).thenReturn(mockJobs);

        HashMap<String, List<Job>> jobByNameMap = jobService.getJobsByNameMap();
        System.out.println(jobByNameMap);

        assert(jobByNameMap.get("TestJob").contains(mockJobs.get(0)));
        assert(jobByNameMap.get("TestJob").contains(mockJobs.get(1)));
        assert(!jobByNameMap.get("TestJob3").contains(mockJobs.get(0)));
    }

    @Test
    public void testAverageDeliveryTimeReturnsCorrectValue() {
        when(jobRepository.findAll()).thenReturn(mockJobs);

        List<Map<String, Long>> jobAverageDeliveries = jobService.averageDeliveryTime();
        HashMap<String, Long> result = new HashMap<>();
        result.put("TestJob", 1500L);
        assert(jobAverageDeliveries.contains(result));
    }

    @Test
    public void testGetRunsPerPeriod() {
        when(jobRepository.findAll()).thenReturn(mockJobs);
        JobService jobService1 = Mockito.spy(jobService);
        // NOW: Fri Oct 22 2021 10:08:20
        Mockito.doReturn(new Date(1634897300000L)).when(jobService1).getCurrentTime();

        mockJobs = new ArrayList<>();
        // Mon Aug 16 2021 17:43:20
        Job mockJob1 = new Job(1, 1629135800000L, "TestJob", 1000L, "SUCCESS");
        // Fri Oct 22 2021 10:03:20
        Job mockJob2 = new Job(2, 1634897000000L, "TestJob", 2000L, "SUCCESS");
        // Mon Oct 18 2021 22:10:00
        Job mockJob3 = new Job(3, 1634595000000L, "TestJob3", 2000L, "FAILURE");
        mockJobs.add(mockJob1);
        mockJobs.add(mockJob2);
        mockJobs.add(mockJob3);

        RunsPerPeriod testJobResult = new RunsPerPeriod("TestJob", 1.0, 1.0, 1.0, 2.0);
        RunsPerPeriod testJob3Result = new RunsPerPeriod("TestJob3", 0.0, 1.0, 1.0, 1.0);

        List<RunsPerPeriod> runsPerPeriodList = jobService1.getRunsPerPeriod();

        assert(runsPerPeriodList.contains(testJobResult));
        assert(runsPerPeriodList.contains(testJob3Result));
    }

    @Test
    public void testGetPassFailRate() {
        mockJobs = new ArrayList<>();
        // Mon Aug 16 2021 17:43:20
        Job mockJob1 = new Job(1, 1629135800000L, "TestJob", 1000L, "SUCCESS");
        // Fri Oct 22 2021 10:03:20
        Job mockJob2 = new Job(2, 1634897000000L, "TestJob", 2000L, "FAILURE");
        mockJobs.add(mockJob1);
        mockJobs.add(mockJob2);

        when(jobRepository.findAll()).thenReturn(mockJobs);
        JobService jobService1 = Mockito.spy(jobService);
        // NOW: Fri Oct 22 2021 10:08:20
        Mockito.doReturn(new Date(1634897300000L)).when(jobService1).getCurrentTime();

        PassFailRate testJobResult = new PassFailRate("TestJob");
        List<PassFailRate> runsPerPeriodList = jobService1.getPassFailRatePerPeriod();

        assert(runsPerPeriodList.get(0).getPerDay().get("SUCCESS") == 0);
        assert(runsPerPeriodList.get(0).getPerDay().get("FAILURE") == 1);

        assert(runsPerPeriodList.get(0).getPerWeek().get("SUCCESS") == 0);
        assert(runsPerPeriodList.get(0).getPerWeek().get("FAILURE") == 1);

        assert(runsPerPeriodList.get(0).getPerPerYear().get("SUCCESS") == 1);
    }


    @Test
    public void testAverageRestoreTime() {
        mockJobs = new ArrayList<>();
        // Mon Aug 16 2021 17:43:20
        Job mockJob1 = new Job(1, 1634896000000L, "TestJob", 1000L, "FAILURE");
        // Fri Oct 22 2021 10:03:20
        Job mockJob2 = new Job(2, 1634897000000L, "TestJob", 2000L, "SUCCESS");
        mockJobs.add(mockJob1);
        mockJobs.add(mockJob2);

        when(jobRepository.findAll()).thenReturn(mockJobs);

        List<Map<String, Long>> averageRestoreTime = jobService.averageRestoreTime();
        assert(averageRestoreTime.get(0).get("TestJob") == 1000000L);
    }
}
