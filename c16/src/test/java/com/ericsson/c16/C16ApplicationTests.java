package com.ericsson.c16;

import com.ericsson.c16.dao.JobRepository;
import com.ericsson.c16.model.Job;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ConfigurationProperties(prefix = "test")
class C16ApplicationTests {

    @Mock
    private JobRepository jobRepository;


    @Test
    public void testfindname() {
        Job newJob1 = new Job(1, 1000000L, "TestJob", 2000L, "SUCCESS");
        jobRepository.save(newJob1);
        assertThat(newJob1.getFullName().contains("TestJob"));
    }

    @Test
    public void testsetname() {
        Job newJob1 = new Job(1, 1000000L, "TestJob", 2000L, "SUCCESS");
        jobRepository.save(newJob1);
        newJob1.setFullName("TestJob1-new");
        assertThat(newJob1.getFullName().contains("TestJob1-new"));
    }
    @Test
    public void TestgetTimeStamp(){
        Job newJob1 = new Job(1, 1000000L, "TestJob", 2000L, "SUCCESS");
        jobRepository.save(newJob1);
        newJob1.setTimestamp(1000001L);
        newJob1.getTimestamp();
        assert(newJob1.getTimestamp() == 1000001L);
    }
    @Test
    public void testSetIDGetID(){
        Job newJob1 = new Job(1, 1000000L, "TestJob", 2000L, "SUCCESS");
        jobRepository.save(newJob1);
        newJob1.setId(2);
        assert(newJob1.getId() == 2);
    }
    @Test
    public void testSetDuration(){
        Job newJob1 = new Job(1, 1000000L, "TestJob", 2000L, "SUCCESS");
        jobRepository.save(newJob1);
        newJob1.setDuration(2001L);
        assert(newJob1.getDuration() == 2001L);
    }
    @Test
    public void testResult(){
        Job newJob1 = new Job(1, 1000000L, "TestJob", 2000L, "SUCCESS");
        jobRepository.save(newJob1);
        newJob1.setResult("FAILURE");
        assert(newJob1.getResult() == "FAILURE");
    }
    @Test
    public void testToString(){
        Job newJob = new Job();
        String expected = "Job{" +

                "id=0" +

                ", timestamp=0" +

                ", fullName='null'" +

                ", duration=0" +

                ", result='null'" +

                '}';
        Assert.assertEquals(expected, newJob.toString());
    }
    @Test
    public void testHash(){
        Job newJob1 = new Job(1, 1000000L, "TestJob", 2000L, "SUCCESS");
        Job newJob2 = new Job(1, 1000000L, "TestJob", 2000L, "SUCCESS");
        Assert.assertTrue(newJob1.equals(newJob2) && newJob2.equals(newJob1));
        Assert.assertTrue(newJob1.hashCode() == newJob2.hashCode());
    }

}

