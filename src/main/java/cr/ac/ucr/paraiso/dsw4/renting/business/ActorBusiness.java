package cr.ac.ucr.paraiso.dsw4.renting.business;

import java.util.List;

import org.springframework.stereotype.Service;

import cr.ac.ucr.paraiso.dsw4.renting.data.ActorData;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Actor;

@Service
public class ActorBusiness {

    private final ActorData actorData;

    public ActorBusiness(ActorData actorData) {
        this.actorData = actorData;
    }

    public List<Actor> findAll() {
        return actorData.findAll();
    }
}
