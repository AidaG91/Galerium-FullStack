package galerium.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Client extends User{

    @NotBlank
    @Schema(description = "Client's phone number.", example = "+34 600 000 000")
    private String phoneNumber;

    @Schema(description = "Client's postal address.", example = "Calle de Ejemplo, 345, Barcelona (Spain)")
    @Size(min = 5, max = 255, message = "The address must be between 5 and 255 characters.")
    private String address;

    @Schema(hidden = true)
    @Column(columnDefinition = "TEXT", nullable = true)
    private String internalNotes;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Gallery> galleries;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "client_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
