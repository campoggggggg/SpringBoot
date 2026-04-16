package com.example.toDoListWithAutentication.todo;


import com.example.toDoListWithAutentication.user.Utente;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;

    private String descrizione;

    private boolean completato;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
}