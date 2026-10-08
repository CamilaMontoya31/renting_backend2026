package cr.ac.ucr.paraiso.dsw4.entity;

import jakarta.persistence.*;

@Entity
@Table(name="actor")
public class Actor {
    @Id
    private Long actorId;
    @Column(name="actor")
    private String nombreActor;
    @Column(name="apellidosActor")
    private String apellidosActor;
}
