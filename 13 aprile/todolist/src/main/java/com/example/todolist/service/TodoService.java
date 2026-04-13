package com.example.todolist.service;

import com.example.todolist.model.Todo;
import com.example.todolist.model.Todo.StatoTask;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    
    public boolean dataValida (Todo todo) {
        if (todo.getDataScadenza().isBefore(LocalDate.now())) {
            return false;
        }
        return true;
    }

    public List<Todo> getScaduti (List<Todo> lista) {
        return lista.stream()
        .filter((Todo t) -> t.getDataScadenza() != null)                          // solo quelli con scadenza
        .filter((Todo t) -> t.getDataScadenza().isBefore(LocalDate.now()))        // scadenza superata
        .filter((Todo t) -> t.getStatoTask() != Todo.StatoTask.DONE)              // non ancora completati
        .collect(Collectors.toList());
    }

    public List<Todo> ordina(List<Todo> lista) {
        return lista.stream()
        .sorted(Comparator
            .comparingInt((Todo t) -> t.getPriorità())                    // prima ordina per priorità (1 viene prima di 3)
            .thenComparing(Todo::getDescrizione))               // a parità di priorità, ordine alfabetico
        .collect(Collectors.toList());      
    }

    public boolean transazioneValida (StatoTask attuale, StatoTask nuovo) {
        if (attuale == StatoTask.TODO) {
            return nuovo == StatoTask.IN_PROGRESS || nuovo == StatoTask.CANCELLED;
        } else if (attuale == StatoTask.IN_PROGRESS) {
            return nuovo == StatoTask.DONE || nuovo == StatoTask.CANCELLED;
        } else { //done o cancelled la task è finita
            return false;
        }
    }
}
