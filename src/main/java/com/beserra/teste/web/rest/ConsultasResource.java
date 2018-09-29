package com.beserra.teste.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class ConsultasResource {

    private final Logger log = LoggerFactory.getLogger(ParametroResource.class);

    @GetMapping("/consultas")
    @Timed
    public String helloWorld() {
        return "Cachaça Carai!!!";
    }
}