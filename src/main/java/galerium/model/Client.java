package galerium.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Client {


    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

}
