package com.example.securenotes.controller;

import com.example.securenotes.model.Note;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final Map<Integer, Note> notes = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger();

    @GetMapping
    public Collection<Note> getAllNotes() {
        return notes.values();
    }

    @PostMapping
    public Note addNote(@RequestBody Note note) {
        int id = idCounter.incrementAndGet();
        note.setId(id);
        notes.put(id, note);
        return note;
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable int id) {
        notes.remove(id);
    }
}
