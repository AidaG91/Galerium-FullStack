package galerium.dto.photo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class PhotoRequestDTO {

    @NotNull(message = "Gallery ID is required")
    private Long galleryId;

    @URL
    @NotNull(message = "Photo URL is required")
    private String url;

    @Size(max = 100)
    private String title;

    private String description;

    @Size(max = 300)
    private String clientComment;

    private boolean favorite = false;

}
