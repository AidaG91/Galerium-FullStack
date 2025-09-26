package galerium.dto.photo;

import lombok.Data;

@Data
public class PhotoResponseDTO {
    private Long id;
    private Long galleryId;
    private String url;
    private String title;
    private String description;
    private String clientComment;
    private boolean favorite;
}
