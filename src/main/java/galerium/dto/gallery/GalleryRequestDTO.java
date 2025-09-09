package galerium.dto.gallery;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GalleryRequestDTO {

    @NotBlank
    @Size(max = 100, message = "The title cannot exceed 100 characters")
    private String title;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private String description;

    @NotNull(message = "Photographer ID is required")
    private Long photographerId;

    @NotNull(message = "Client ID is required")
    private Long clientId;

}
