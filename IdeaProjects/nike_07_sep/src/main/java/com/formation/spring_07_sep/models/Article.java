package com.formation.spring_07_sep.models;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long ref;

    @Column(nullable = false) // c'est hibernate
    @Pattern(regexp = "^[a-z 0-9.\\-_\\\\/À-ÖØ-öø-ÿ]{3,255}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.DOTALL}) // c'est du regex / Pattern.Flag gère les mayuscule
    @NonNull
    private String title;

    @Column(nullable = false)
    private String link;

    @Column(columnDefinition = "text")
    @NonNull
    private String description;


}
