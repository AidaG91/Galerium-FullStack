package galerium.model;

import jakarta.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private boolean favorite;

    @ManyToOne
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;


}
