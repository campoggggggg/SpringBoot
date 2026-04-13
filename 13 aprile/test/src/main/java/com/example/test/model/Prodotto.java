package com.example.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   //getter e setter
@NoArgsConstructor      //costruttore vuoto
@AllArgsConstructor     //costruttore con variabili

public class Prodotto {
    private Long id;
    private String nome;
    private double prezzo;
}