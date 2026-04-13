package com.example.test.service;

import org.springframework.stereotype.Service;

@Service
public class NotificaService {
    public void inviaNotifica(String testo) {
        System.out.println("Notifica inviata: " + testo);
    }
}
