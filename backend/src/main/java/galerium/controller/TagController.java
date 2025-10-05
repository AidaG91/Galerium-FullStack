package galerium.controller;

import galerium.service.interfaces.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<String>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteTag(@PathVariable String name) {
        try {
            tagService.deleteTagByName(name);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}