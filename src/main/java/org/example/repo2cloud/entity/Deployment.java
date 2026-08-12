package org.example.repo2cloud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.repo2cloud.entity.models.Status;
import org.hibernate.annotations.IdGeneratorType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String url;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime requestedAt;

    private Integer port;

    private String deployedUrl;
}
