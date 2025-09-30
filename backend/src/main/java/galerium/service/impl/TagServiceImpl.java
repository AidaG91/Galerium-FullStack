package galerium.service.impl;

import galerium.model.Tag;
import galerium.repository.TagRepository;
import galerium.service.interfaces.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<String> getAllTags() {
        return tagRepository.findAll().stream()
                .map(Tag::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}