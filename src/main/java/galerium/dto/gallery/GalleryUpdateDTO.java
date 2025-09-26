package galerium.dto.gallery;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GalleryUpdateDTO {

    @NotBlank
    @Size(max = 100, message = "The title cannot exceed 100 characters")
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String description;
}
