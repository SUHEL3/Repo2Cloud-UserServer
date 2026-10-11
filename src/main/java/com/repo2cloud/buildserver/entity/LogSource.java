package com.repo2cloud.buildserver.entity;

/**
 * Identifies which phase of the deployment pipeline produced a log entry.
 * Stored as a VARCHAR so filtering by source is straightforward in both SQL
 * and application code without joins.
 */
public enum LogSource {

    /** Cloning phase: git clone output and related events. */
    GIT,

    /** Build detection + image build phase (Dockerfile, Railpack, etc.). */
    BUILD,

    /** Container orchestration: port allocation, docker run, health checks. */
    DEPLOY,

    /** General platform/system events not tied to a specific pipeline stage. */
    SYSTEM
}
