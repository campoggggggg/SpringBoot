package com.example.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.service.CalcolatriceService;
import com.example.test.service.MessaggioService;

@RestController
public class CalcolatriceController {

    private final CalcolatriceService calcolatriceService;

    //costruttore injection
    @Autowired
    public CalcolatriceController(CalcolatriceService calcolatriceService) {
        this.calcolatriceService = calcolatriceService;
    }

    @GetMapping("/somma")
    public int somma(@RequestParam int a, @RequestParam int b) {
        return calcolatriceService.somma(a, b);
    }
}

