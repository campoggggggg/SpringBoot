package com.example.todolist.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class Todo {

    public enum StatoTask {
        TODO, IN_PROGRESS, DONE, CANCELLED
    }

    private Long id;
    private String descrizione;
    private StatoTask statoTask;
    private int priorità;
    private LocalDate dataScadenza;

/*
public LocalDate getDataScadenza() {
        return dataScadenza;
    }
    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
    public int getPriorità() {
        return priorità;
    }
    public void setPriorità(int priorità) {
        if(priorità < 1 || priorità > 4) {
            System.out.println("Livello priorità non consentito. scegli 1 | 2 | 3 ");
            return;
        }
        this.priorità = priorità;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public StatoTask getStatoTask() {
        return statoTask;
    }
    public void setStatoTask(StatoTask statoTask) {
        this.statoTask = statoTask;
    }

*/

}