package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="JOB")
@IdClass(JobId.class)
public class Job {
	@Id
	private long timestamp;
	@Id
	private String fullName;
	private long id;
	private long duration;
	private String result;

	public Job() {
	}

	public Job(long id, Long timestamp, String fullName, Long duration, String result) {
		this.id = id;
		this.timestamp = timestamp;
		this.fullName = fullName;
		this.duration = duration;
		this.result = result;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Job job = (Job) o;
		return id == job.id && Objects.equals(timestamp, job.timestamp) && Objects.equals(fullName, job.fullName) && Objects.equals(duration, job.duration) && Objects.equals(result, job.result);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, timestamp, fullName, duration, result);
	}

	@Override
	public String toString() {
		return "Job{" +
				"id=" + id +
				", timestamp=" + timestamp +
				", fullName='" + fullName + '\'' +
				", duration=" + duration +
				", result='" + result + '\'' +
				'}';
	}
}
