package com.example.todolist.service;

import com.example.todolist.model.Todo;
import com.example.todolist.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    // 1. Inietto il repository invece della lista in memoria
    private final TodoRepository repository;

public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<Todo> getAll() {
        // 2. Uso i metodi pronti di CrudRepository
        return (List<Todo>) repository.findAll();
    }

    public Optional<Todo> getById(Long id) {
        return repository.findById(id);
    }

    public Todo create(Todo nuovo) {
        // 3. Non serve più idCounter++, lo fa il DB (GenerationType.IDENTITY)
        return repository.save(nuovo);
    }

    public Optional<Todo> update(Long id, Todo modificato) {
        return repository.findById(id).map(todo -> {
            todo.setDescrizione(modificato.getDescrizione());
            todo.setCompletato(modificato.isCompletato());
            return repository.save(todo); // Salvo le modifiche nel DB
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}