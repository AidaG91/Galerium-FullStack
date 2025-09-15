package galerium.dto.gallery;

import com.fasterxml.jackson.annotation.JsonFormat;
import galerium.dto.photo.PhotoResponseDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GalleryResponseDTO {
    private Long id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String description;

    private Long photographerId;

    private Long clientId;

    private List<PhotoResponseDTO> photos;
}

