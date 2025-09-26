package galerium.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "photos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    @URL
    @NotNull
    @Schema(description = "URL of the photo.", example = "https://example.com/photos/123.jpg")
    @Column(nullable = false)
    private String url;

    @Schema(description = "Optional title for the photo.", example = "Party time!")
    @Size(max = 100)
    @Column(nullable = true)
    private String title;

    @Schema(description = "Photographer's optional description of the photo")
    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @Schema(description = "Comment added by the client")
    @Size(max = 300)
    @Column(columnDefinition = "TEXT", nullable = true)
    private String clientComment;

    @Schema(description = "Whether the client marked this photo as a favorite")
    @Column(nullable = false)
    private boolean favorite = false;

}
