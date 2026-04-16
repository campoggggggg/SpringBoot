package com.example.toDoListWithAutentication.todo;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public Todo crea(@RequestBody Todo todo, Authentication authentication) {
        String email = authentication.getName();
        return todoService.creaTodo(todo, email);
    }

    @GetMapping
    public List<Todo> getTutti(Authentication authentication) {
        String email = authentication.getName();
        return todoService.getTodosUtente(email);
    }

    @PutMapping("/{id}")
    public Todo aggiorna(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.aggiornaTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    public void elimina(@PathVariable Long id) {
        todoService.eliminaTodo(id);
    }
}