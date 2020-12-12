package com.panthers.main.jobmodel;

public enum JobStatus {
    PENDING,
    QUEUED,
    STARTING_ALGORITHM,
    GENERATING_DISTRICTS,
    RUNNING,
    POST_PROCESSING,
    COMPLETED,
    CANCELLED,
    ERROR
}
