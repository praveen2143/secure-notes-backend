package com.example.securenotes.controller;

import com.example.securenotes.model.Note;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final Map<String, List<Note>> userNotes = new ConcurrentHashMap<>();

    private String getUserEmail() {
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal().getAttribute("email");
    }

    @GetMapping
    public List<Note> getNotes() {
        return userNotes.getOrDefault(getUserEmail(), new ArrayList<>());
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        note.setId(UUID.randomUUID().toString());
        userNotes.computeIfAbsent(getUserEmail(), k -> new ArrayList<>()).add(note);
        return note;
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable String id, @RequestBody Note note) {
        List<Note> notes = userNotes.get(getUserEmail());
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(id)) {
                notes.set(i, note);
                return note;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        List<Note> notes = userNotes.get(getUserEmail());
        notes.removeIf(note -> note.getId().equals(id));
    }
}

