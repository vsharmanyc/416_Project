package com.panthers.main.api;

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
