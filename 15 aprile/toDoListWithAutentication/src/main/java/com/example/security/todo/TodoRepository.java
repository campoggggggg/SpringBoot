package com.example.toDoListWithAutentication.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.toDoListWithAutentication.user.Utente;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUtente(Utente utente);
}