package galerium.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "photographers")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Photographer extends User {

    @OneToMany(mappedBy = "photographer")
    private List<Gallery> galleries;

    @OneToMany(mappedBy = "photographer")
    private List<Client> clients;
}


