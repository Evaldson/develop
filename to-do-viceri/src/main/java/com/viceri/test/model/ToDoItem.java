package com.viceri.test.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.viceri.test.enums.Priority;

import lombok.Data;

@Entity
@Data
public class ToDoItem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	private Priority priority; 
	
    private String description;
    
    @Value(value = "false")
    private boolean completed;
    
    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
}
