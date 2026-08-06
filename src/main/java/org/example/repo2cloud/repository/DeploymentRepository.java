package org.example.repo2cloud.repository;

import org.example.repo2cloud.entity.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment,Long> {
}
