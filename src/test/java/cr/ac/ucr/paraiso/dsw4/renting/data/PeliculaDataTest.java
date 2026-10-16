package cr.ac.ucr.paraiso.dsw4.renting.data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Actor;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Genero;


@SpringBootTest
public class PeliculaDataTest {

    @Autowired
    private PeliculaData peliculaData;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Test
    @Sql (scripts = "/save_pelicula_con_actores.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void save_pelicula_con_actores() {
        //  IDENT_CURRENT() returns the last identity value generated for a specific table, regardless of the scope.
        // Retrieve generated genero_id
        Integer generoId = jdbcTemplate.queryForObject("SELECT IDENT_CURRENT('Genero')", Integer.class);

        // Retrieve generated actor1_id and actor2_id
        Integer actorId1 = jdbcTemplate.queryForObject("SELECT IDENT_CURRENT('Actor')-1", Integer.class);
        Integer actorId2 = jdbcTemplate.queryForObject("SELECT IDENT_CURRENT('Actor')", Integer.class);

        List<Actor> actores = new LinkedList<>();
        actores.add(new Actor(actorId1, null, null));
        actores.add(new Actor(actorId2, null, null));
       
        Pelicula pelicula = new Pelicula(0, "The Matrix", true, true,
        new Genero(generoId, null), actores);
       
        //Act
        assertDoesNotThrow(() -> peliculaData.save(pelicula));
        // Assert
        assertNotEquals(0, pelicula.getPeliculaId());
    }

}
