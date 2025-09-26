package galerium.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientRequestDTO {

    @NotBlank(message = "Full name cannot be blank.")
    @Size(min = 3, max = 150, message = "Full name must be between 3 and 150 characters.")
    private String fullName;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Please provide a valid email address.")
    @Size(min = 5, max = 150, message = "Email must be between 5 and 150 characters.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters.")
    private String password;

    private String profilePictureUrl;

    @NotNull
    private String phoneNumber;

    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    private String address;
}
