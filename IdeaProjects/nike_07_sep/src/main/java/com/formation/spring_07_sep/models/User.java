package com.formation.spring_07_sep.models;

import com.formation.spring_07_sep.emums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id // c'est Hibernate
    @Email // c'est avec le java
    @NotNull // c'est avec le java
    @NotBlank // c'est avec le java
    private String email;

    @Column(length = 155, nullable = false)
    @Pattern(regexp = "^[a-z \\-À-ÖØ-öø-ÿ]{2,155}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.DOTALL},
            message = "Il ne doit contenir que des lettre miniscules ou majuscule, un space ou un tiret")
    private String firstname;

    @Column(length = 155, nullable = false)
    @Pattern(regexp = "^[a-z \\-À-ÖØ-öø-ÿ]{2,155}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.DOTALL},
            message = "{perso.javax.constraints.name}")
    private String lastname;

    @Transient
    @Pattern(regexp = "^.{6,32}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.DOTALL})
    private String mdp;

    @Column(length = 150, nullable = false)
    private String mdpHash;

    @Column(length = 150, nullable = false)
    private String salt;

    @Enumerated(EnumType.STRING) // pour le hibernate de mettre le string de mon enum
    private RoleEnum role;

}
