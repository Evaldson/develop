package com.viceri.test.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;

@Entity
@Data
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
   @NotBlank
   @NotNull
    private String username;
   
   @NotBlank
   @NotNull
   @Email
   @Column(unique = true)
    private String email;
    
   @NotBlank
   @NotNull
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
   @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonManagedReference
    private List<ToDoItem> tasks = new ArrayList<>();
    
}
