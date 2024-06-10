package com.battlecodes.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user_model", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "&quot;Электронная почта&quot;", example = "junior@example.com")
    @Email
    @NotBlank
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password", length = 2000)
    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupModel group;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<ERole> roles = new HashSet<>();

}
