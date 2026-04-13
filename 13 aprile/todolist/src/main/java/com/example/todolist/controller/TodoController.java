package com.example.todolist.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.todolist.model.Todo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.service.TodoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/todo")
public class TodoController {
    private List<Todo> todoList = new ArrayList<>();
    private Long idCounter = 1L;
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAll () {
        return todoService.ordina(todoList);
    }
    
    @GetMapping("/scaduti")
    public List<Todo> getScaduti() {
        return todoService.getScaduti(todoList);
    }
    
    @PostMapping
    public Todo crea(@RequestBody Todo nuovo) {
        if (!todoService.dataValida(nuovo)) {
            System.out.println("Data passata");
            return null;
        }
        nuovo.setId(idCounter++); //setto con id progressivo
        todoList.add(nuovo);
        return nuovo;
    }
    
    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id) {
        return todoList.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
    
    @PutMapping("/{id}")
    public Todo aggiorna(@PathVariable Long id, @RequestBody Todo modificato) {
        for (Todo p : todoList) {
            if (p.getId().equals(id)) {

                if(!todoService.transazioneValida(p.getStatoTask(), modificato.getStatoTask())) {
                    System.out.println("Errore: transizione non consentita: ");
                    return null;
                }

                p.setDescrizione(modificato.getDescrizione());
                p.setStatoTask(modificato.getStatoTask());
                return p;
            }
        }
        
        return null;
    }
    
    @DeleteMapping("/{id}")
        public String elimina(@PathVariable Long id) {
        todoList.removeIf(p -> p.getId().equals(id));
        return "Prodotto eliminato con successo.";
    }
}
