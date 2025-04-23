package com.hearthwarrio.auth.controller;

import com.hearthwarrio.auth.model.User;
import com.hearthwarrio.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AuthController {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Value("${jwt.secret}")
    private String jwtSecret;
    public AuthController(UserRepository users) { this.users = users; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User u) {
        u.setPassword(encoder.encode(u.getPassword()));
        users.save(u);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {
        User found = users.findByEmail(u.getEmail());
        if (found == null || !encoder.matches(u.getPassword(), found.getPassword())) {
            return ResponseEntity.status(401).body("Bad credentials");
        }
        String token = Jwts.builder()
                .setSubject(found.getId().toString())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
        return ResponseEntity.ok(token);
    }
}
