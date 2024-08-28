package com.ericsson.c16;

import com.ericsson.c16.model.Job;
import com.ericsson.c16.model.RunsPerPeriod;
import com.ericsson.c16.service.JenkinsService;
import com.ericsson.c16.service.JobService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ConfigurationProperties(prefix = "test")
@Profile("test")
@SpringBootTest
class JenkinsServiceTests {

    @Spy
    JenkinsService jenkinsServiceSpy = new JenkinsService();

    @Test
    public void testBuildHistoryToObjectConverterListSize() {
        String mockJsonData = "{ \"_class\":\"hudson.model.Hudson\", \"jobs\":[ { \"_class\":\"hudson.model.FreeStyleProject\", \"name\":\"Admin_DSL_PreCodeReview\", \"builds\":[ { \"_class\":\"hudson.model.FreeStyleBuild\", \"duration\":7965, \"id\":\"15\", \"result\":\"FAILURE\", \"timestamp\":1634310480926 }, { \"_class\":\"hudson.model.FreeStyleBuild\", \"duration\":18629, \"id\":\"14\", \"result\":\"SUCCESS\", \"timestamp\":1633687408065 } ] }, { \"_class\":\"hudson.model.FreeStyleProject\", \"name\":\"Admin_DSL_Publish\", \"builds\":[ { \"_class\":\"hudson.model.FreeStyleBuild\", \"duration\":2717, \"id\":\"9\", \"result\":\"SUCCESS\", \"timestamp\":1633687418076 } ] } ]}";

//        JenkinsService jenkinsServiceSpy = Mockito.spy(jenkinsService);
        Mockito.doReturn(mockJsonData).when(jenkinsServiceSpy).getWebResource(any());

        ArrayList<Job> buildHistoryAsObject = (ArrayList<Job>) jenkinsServiceSpy.getBuildHistory();
        System.out.println(buildHistoryAsObject);

        assert(buildHistoryAsObject.size() == 3);
    }

    @Test
    public void testRunsPerPeriodEquals() {
        RunsPerPeriod runsPerPeriod1 = new RunsPerPeriod("Test1", 1.0, 2.0, 20.0, 50.0);
        RunsPerPeriod runsPerPeriod2 = new RunsPerPeriod("Test1", 1.0, 2.0, 20.0, 50.0);

        assert(runsPerPeriod1.equals(runsPerPeriod2));
    }
}
