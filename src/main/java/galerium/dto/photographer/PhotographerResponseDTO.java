package galerium.dto.photographer;

import com.fasterxml.jackson.annotation.JsonFormat;
import galerium.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhotographerResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String profilePictureUrl;
    private Boolean isEnabled;
    private UserRole userRole;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime registrationDate;
}
