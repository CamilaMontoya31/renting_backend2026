package cr.ac.ucr.paraiso.dsw4.renting.business;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import cr.ac.ucr.paraiso.dsw4.renting.data.PeliculaData;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;

@Service
public class PeliculaBusiness {
    //hay dos formas de inyectar 1.notacion    @Autowired y esta:
    private final PeliculaData peliculaData;

    public PeliculaBusiness(PeliculaData peliculaData){
        this.peliculaData = peliculaData;
    }

    public List<Pelicula> findMoviesByTitleOrGenre(String title, String genre){
        return peliculaData.findMoviesByTitleOrGenre(title, genre);
    }

    public Pelicula save(Pelicula pelicula) throws SQLException {
        return peliculaData.save(pelicula);
    }
}
