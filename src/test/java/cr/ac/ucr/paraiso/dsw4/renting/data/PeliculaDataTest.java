package cr.ac.ucr.paraiso.dsw4.renting.data;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

@SpringBootTest
public class PeliculaDataTest {
     @Autowired
    private PeliculaData peliculaData;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    //Given existing movies, when searching by existing title and existing genre, the returns movies
    @DisplayName("Debe retornar la(s) película(s) cuando el título y el género existen en la base de datos")
    @Transactional // Para que los datos se borren automáticamente al terminar el test
    @Sql(scripts = "/insert_peliculas_con_actores.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)


    //Carga datos de prueba antes de ejecutar la prueba
    public void givenExistingMovies_withExistingTitleAndExistingGenre_thenReturnsMovies() {
        // Arrange
            String title = "Story";
            String genre = "Drama";
        
        // Act
            //var peliculas = peliculaData.findMoviesByTitleOrGenre(title, genre); // MUT method under test
            List<Pelicula> peliculas = peliculaData.findMoviesByTitleOrGenre(title, genre);
        
        // Assert

        assertNotNull(peliculas);
        assertTrue(!peliculas.isEmpty() == false);

        String expectedTitle = "Story";
        String expectedGenre = "Drama";

        assertTrue(peliculas.stream().anyMatch(p -> p.getTitulo().contains(expectedTitle) || 
         p.getGenero().getNombreGenero().contains(expectedGenre)));

}
}
