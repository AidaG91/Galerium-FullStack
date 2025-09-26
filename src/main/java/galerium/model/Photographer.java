package galerium.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "photographers")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Photographer extends User {

    @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL)
    private List<Gallery> galleries;
}


