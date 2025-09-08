package galerium.model;

import galerium.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "User's email address", example = "user@example.com", maxLength = 150)
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Please add a valid email address.")
    @Size(min = 5, max = 150, message = "Your email cannot exceed 150 characters." )
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(hidden = true)
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters.")
    @Column(nullable = false)
    private String password;

    @Schema(description = "User's full name.", example = "John Wayne")
    @Column(nullable = false)
    @Size(min = 3, max = 150, message = "Your full name must have at least 3 characters." )
    private String fullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = true;

    @Schema(description = "Date when the user registered.")
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime registrationDate;

    @Schema(description = "URL of the user's profile picture", example = "https://example.com/images/user123.jpg")
    @Column(nullable = true)
    private String profilePictureUrl;

}