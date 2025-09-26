package galerium.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import galerium.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClientResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String profilePictureUrl;
    private String phoneNumber;
    private String address;
    private Boolean isEnabled;
    private UserRole userRole;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

}
