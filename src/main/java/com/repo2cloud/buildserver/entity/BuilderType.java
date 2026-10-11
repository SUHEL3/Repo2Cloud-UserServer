package com.repo2cloud.buildserver.entity;

/**
 * Identifies which build strategy produced the Docker image for a deployment.
 * Persisted as a VARCHAR in the deployments table so it is human-readable in
 * the DB and forward-compatible when new strategy values are added.
 */
public enum BuilderType {

    /**
     * Repository contained a Dockerfile at the root; built via {@code docker build}.
     * Developer-supplied Dockerfiles always take priority over auto-detection.
     */
    DOCKERFILE,

    /**
     * No Dockerfile was present; built via the Railpack CLI ({@code railpack build}),
     * which auto-detects the language/framework and produces a Docker image.
     */
    RAILPACK,

    /**
     * No Dockerfile was present; built via the Nixpacks CLI ({@code nixpacks build}),
     * which auto-detects the language/framework and produces a Docker image.
     */
    NIXPACKS,

    /**
     * Strategy could not be determined (e.g. legacy records created before
     * this field was introduced, or a future strategy not yet named here).
     */
    UNKNOWN
}
