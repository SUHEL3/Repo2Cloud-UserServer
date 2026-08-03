package org.example.repo2cloud.repository;

import org.example.repo2cloud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
