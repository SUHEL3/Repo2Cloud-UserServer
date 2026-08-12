package org.example.repo2cloud.repository;

import org.example.repo2cloud.entity.Deployment;
import org.example.repo2cloud.entity.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface DeploymentRepository extends JpaRepository<Deployment,Long> {
    Optional<List<Deployment>> getDeploymentsByUserId(Long userId);

    @Query("""
        SELECT d.status AS status FROM Deployment d WHERE d.id = :deploymentId
    """)
    Optional<Status> getStatus(
            @Param("deploymentId") Long deploymentId
    );
}
