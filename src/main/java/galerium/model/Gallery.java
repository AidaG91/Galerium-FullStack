package galerium.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "galleries")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String title;
    private LocalDateTime date;
    private boolean downloadable;
    private boolean shared;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL)
    private List<Photo> photos;

// Puedes añadir campos como downloadPermission, sharePermission, etc.
}
