package galerium.service.impl;

import galerium.model.Tag;
import galerium.repository.ClientRepository;
import galerium.repository.TagRepository;
import galerium.service.interfaces.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ClientRepository clientRepository;

    @Override
    public List<String> getAllTags() {
        return tagRepository.findAll().stream()
                .map(Tag::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTagByName(String tagName) {
        if (clientRepository.existsByTagsName(tagName)) {
            throw new IllegalArgumentException("Cannot delete tag '" + tagName + "' because it is currently in use.");
        }

        Tag tagToDelete = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("Tag '" + tagName + "' not found."));

        tagRepository.delete(tagToDelete);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupOrphanTags() {
        System.out.println("--- Running orphan tags cleanup task ---");
        List<Tag> orphanTags = tagRepository.findOrphanTags();
        if (!orphanTags.isEmpty()) {
            System.out.println("Found " + orphanTags.size() + " orphan tags to delete.");
            tagRepository.deleteAll(orphanTags);
        } else {
            System.out.println("No orphan tags found.");
        }
        System.out.println("--- Orphan tags cleanup task finished ---");
    }
}