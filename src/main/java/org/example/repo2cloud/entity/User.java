package org.example.repo2cloud.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.repo2cloud.entity.models.Role;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    private Float credits;

}
