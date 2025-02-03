/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util.quatz;

/**
 * @author Bubble
 * @date 2025/01/27 22:46
 */
public class Job implements Comparable<Job>{

    private String jobName;
    private Long startTime;
    private Long delay;
    private Runnable task;

    public Job() {
    }

    public Job(String jobName, Long startTime, Long delay, Runnable task) {
        this.jobName = jobName;
        this.startTime = startTime;
        this.delay = delay;
        this.task = task;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    /**
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Job o) {
        return this.startTime.compareTo(o.startTime);
    }
}
