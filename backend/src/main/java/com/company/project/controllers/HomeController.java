package com.company.project.controllers;

import com.company.project.entity.Greeting;
import com.company.project.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private GreetingRepository repository;

    @GetMapping("/")
    public Greeting showHome(String name, Model model) {
        return repository.findById(1).orElse(new Greeting("Not Found 😕"));
    }

    @GetMapping("/greetings")
    public List<Greeting> greetings() {
        List<Greeting> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        result.sort(Comparator.comparingInt(Greeting::getId));
        return result;
    }

    @GetMapping("/greetings/{id}")
    public ResponseEntity<Greeting> greeting(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        Map<String, Object> result = new HashMap<>();
        result.put("service", "Greeting API");
        result.put("database", "MariaDB");
        result.put("totalGreetings", repository.count());
        return result;
    }
}
