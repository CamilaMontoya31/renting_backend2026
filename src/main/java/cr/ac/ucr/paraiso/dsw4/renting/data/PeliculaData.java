package cr.ac.ucr.paraiso.dsw4.renting.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import cr.ac.ucr.paraiso.dsw4.renting.domain.Actor;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Genero;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import java.sql.Types;

/* 
@Repository
public class PeliculaData {

    private final JdbcTemplate jdbcTemplate;

   PeliculaData(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

    public List<Pelicula> findMoviesByTitleOrGenre(String title, String genre){
        String sqlSelect = """
                        SELECT
                        p.pelicula_id,
                        p.titulo,
                        p.genero_id,
                        g.nombre_genero,
                        p.subtitulada,
                        p.estreno,
                        pa.actor_id,
                        a.nombre_actor,
                        a.apellidos_actor
                    FROM Pelicula p
                    INNER JOIN Genero g
                        ON p.genero_id = g.genero_id
                    LEFT JOIN PeliculaActor pa
                        ON p.pelicula_id = pa.pelicula_id
                        LEFT JOIN Actor a
                        ON pa.actor_id = a.actor_id

                    WHERE LOWER(p.titulo) LIKE ?
                        OR LOWER(g.nombre_genero) LIKE ?;
                """;
                title = title.toLowerCase();
                genre = genre.toLowerCase();

            //esto es para pasarle el titulo y genero
                String titleLike = (title == null || title == "" ? "" : "%" +title.trim() +"%");
                String genreLike = (genre == null || genre == "" ? "" : "%" +genre.trim()+"%");

                return jdbcTemplate.query(sqlSelect,new PeliculaExtractor(), titleLike, genreLike);


    }
}

class PeliculaExtractor implements ResultSetExtractor<List<Pelicula>> {

    @Override
    public List<Pelicula> extractData(ResultSet rs) throws SQLException, DataAccessException {
        // este metodo es implementado
        Map<Integer,Pelicula> map = new HashMap<>();
        Pelicula pelicula = null;

        while(rs.next()){ //le pregunta al ResultSet si tiene registros por recorrer
           int peliculaId = rs.getInt("pelicula_id");
           pelicula = map.get(peliculaId);

           if (pelicula == null) {
            pelicula = new Pelicula();
            pelicula.setPeliculaId(peliculaId);
            pelicula.setTitulo(rs.getString("titulo"));
            
            Genero genero = new Genero();
            genero.setGeneroId(rs.getInt("genero_id"));
            genero.setNombreGenero(rs.getString("nombre_genero"));
            
            pelicula.setGenero(genero);
            pelicula.setSubtitulada(rs.getBoolean("subtitulada"));
            pelicula.setEstreno(rs.getBoolean("estreno"));
            map.put(peliculaId,pelicula);

           } //if
           int actorId = rs.getInt("actor_id");
           
           if(actorId > 0){
            Actor actor = new Actor();
            actor.setActorId(actorId);
            actor.setNombreActor(rs.getString("nombre_actor"));
            actor.setApellidosActor(rs.getString("apellidos_actor"));
            
            pelicula.getActores().add(actor);//ojo
           }
        }//while
        return new ArrayList<Pelicula>(map.values());
    }
    
}
*/


@Repository
public class PeliculaData {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Pelicula> findMoviesByTitleOrGenre(String title, String genre) {
        String sql = """
                SELECT p.pelicula_id, p.titulo, p.genero_id, g.nombre_genero,
                       p.subtitulada, p.estreno, pa.actor_id,
                       a.nombre_actor, a.apellidos_actor
                FROM Pelicula p
                INNER JOIN Genero g ON p.genero_id = g.genero_id
                LEFT JOIN PeliculaActor pa ON p.pelicula_id = pa.pelicula_id
                LEFT JOIN Actor a ON pa.actor_id = a.actor_id
                WHERE LOWER(p.titulo) LIKE ?
                   OR LOWER(g.nombre_genero) LIKE ?
                """;

        String titleLike = "%" + (title == null ? "" : title.trim().toLowerCase()) + "%";
        String genreLike = "%" + (genre == null ? "" : genre.trim().toLowerCase()) + "%";
        return jdbcTemplate.query(sql, new PeliculaExtractor(), titleLike, genreLike);
    }

    @Transactional
    public Pelicula save(Pelicula pelicula) throws SQLException{
        //
            SimpleJdbcCall simpleJdbcCallPelicula = new SimpleJdbcCall(jdbcTemplate).
                    withCatalogName("dbo").
                    withProcedureName("InsertPelicula").withoutProcedureColumnMetaDataAccess().
                    declareParameters(new SqlOutParameter("@pelicula_id", Types.INTEGER)).
                    declareParameters(new SqlParameter("@titulo", Types.VARCHAR)).
                    declareParameters(new SqlParameter("@subtitulada", Types.BIT)).
                    declareParameters(new SqlParameter("@estreno", Types.BIT)).
                    declareParameters(new SqlParameter("@genero_id", Types.INTEGER));
            Map<String, Object> outParameters = simpleJdbcCallPelicula.execute(pelicula.getTitulo(), pelicula.isSubtitulada(), pelicula.isEstreno(), pelicula.getGenero().getGeneroId());
            pelicula.setPeliculaId(Integer.parseInt(outParameters.get("@pelicula_id").toString()));
           
            SimpleJdbcCall simpleJdbcCallPeliculaActor = new SimpleJdbcCall(jdbcTemplate).
                    withCatalogName("dbo").
                    withProcedureName("InsertPeliculaActor").withoutProcedureColumnMetaDataAccess().
                    declareParameters(new SqlParameter("@pelicula_id", Types.INTEGER)).
                    declareParameters(new SqlParameter("@actor_id", Types.INTEGER));
            for(Actor actor:pelicula.getActores())
                simpleJdbcCallPeliculaActor.execute(pelicula.getPeliculaId(), actor.getActorId());
        return pelicula;
    }

    private static final class PeliculaExtractor implements ResultSetExtractor<List<Pelicula>> {
        @Override
        public List<Pelicula> extractData(ResultSet rs) throws SQLException {
            Map<Integer, Pelicula> peliculas = new HashMap<>();

            while (rs.next()) {
                int peliculaId = rs.getInt("pelicula_id");
                Pelicula pelicula = peliculas.get(peliculaId);

                if (pelicula == null) {
                    pelicula = new Pelicula();
                    pelicula.setPeliculaId(peliculaId);
                    pelicula.setTitulo(rs.getString("titulo"));

                    Genero genero = new Genero(
                            rs.getInt("genero_id"),
                            rs.getString("nombre_genero"));
                    pelicula.setGenero(genero);
                    pelicula.setSubtitulada(rs.getBoolean("subtitulada"));
                    pelicula.setEstreno(rs.getBoolean("estreno"));
                    peliculas.put(peliculaId, pelicula);
                }

                int actorId = rs.getInt("actor_id");
                if (actorId > 0) {
                    pelicula.getActores().add(new Actor(
                            actorId,
                            rs.getString("nombre_actor"),
                            rs.getString("apellidos_actor")));
                }
            }

            return new ArrayList<>(peliculas.values());
        }
    }
}
