package com.panthers.main.api;

import com.panthers.main.services.EntityManagerLoad;
import com.panthers.main.services.MapHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/em")
public class TestController {
//    private EntityManagerLoad em;
//
//    @Autowired
//    public TestController(EntityManagerLoad em) {
//        this.em = em;
//    }
//
//    @GetMapping("/getPrecincts")
//    public void getPrecinct(){
//        em.loadEM();
//    }
}
