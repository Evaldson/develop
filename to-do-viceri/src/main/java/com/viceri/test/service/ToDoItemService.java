package com.viceri.test.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.viceri.test.model.Usuario;
import com.viceri.test.enums.Priority;
import com.viceri.test.model.ToDoItem;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ToDoItemService {

	public List<ToDoItem> getTasksByPriority(Usuario usuario, String priority) {
        List<ToDoItem> tasks = usuario.getTasks();
        
        List<ToDoItem> filteredTasks = tasks.stream()
                .filter(task -> !task.isCompleted())
                .filter(task -> task.getPriority().equals(Priority.valueOf(priority)))
                .collect(Collectors.toList());
        
        return filteredTasks;
    }
	
	public List<ToDoItem> getTasks(Usuario usuario) {
        List<ToDoItem> tasks = usuario.getTasks();
        
        List<ToDoItem> filteredTasks = tasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
        
        return filteredTasks;
    }
	
	
}
