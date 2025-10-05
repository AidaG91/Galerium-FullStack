package galerium.service.interfaces;

import java.util.List;

public interface TagService {
    List<String> getAllTags();

    void deleteTagByName(String tagName);
}
