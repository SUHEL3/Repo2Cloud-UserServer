package org.example.repo2cloud.repository;

import org.example.repo2cloud.entity.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeploymentRepository extends JpaRepository<Deployment,Long> {
    Optional<List<Deployment>> getDeploymentsByUserId(Long userId);
}
