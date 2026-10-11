package com.repo2cloud.buildserver.entity;

/**
 * Lifecycle states for a Repo2Cloud deployment.
 *
 * <pre>
 * PENDING
 *   → CLONING   (git clone in progress)
 *   → BUILDING  (build strategy detection + image build)
 *   → DEPLOYING (port allocation + docker run)
 *   → STARTING  (container launched; health-check polling in progress)
 *   → RUNNING   (health check passed; application is reachable)
 *   → FAILED    (any stage failed; see failure_reason)
 *   → STOPPED   (manually stopped by owner; container is not running)
 * </pre>
 */
public enum DeploymentStatus {
    /** Initial state; request accepted but pipeline has not started yet. */
    PENDING,

    /** {@code git clone} is in progress. */
    CLONING,

    /** Build strategy selected and image build is in progress. */
    BUILDING,

    /** Port allocated; {@code docker run} issued. */
    DEPLOYING,

    /**
     * Container has started; health checker is polling until the
     * application is reachable or the timeout expires.
     */
    STARTING,

    /** Health check passed; application is serving traffic on the allocated port. */
    RUNNING,

    /** One or more pipeline stages failed; see {@code failure_reason} for details. */
    FAILED,

    /** Deployment was manually stopped; container is not running. */
    STOPPED
}
