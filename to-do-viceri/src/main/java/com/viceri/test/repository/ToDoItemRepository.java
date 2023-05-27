package com.viceri.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viceri.test.model.ToDoItem;


@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long>{

}
