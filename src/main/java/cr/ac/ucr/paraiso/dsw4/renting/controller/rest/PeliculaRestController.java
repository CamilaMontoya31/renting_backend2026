package cr.ac.ucr.paraiso.dsw4.renting.controller.rest;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.ucr.paraiso.dsw4.renting.business.PeliculaBusiness;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;
import cr.ac.ucr.paraiso.dsw4.renting.dto.PeliculaCreationDTO;
import cr.ac.ucr.paraiso.dsw4.renting.mapper.PeliculaMapper;

@RestController
@RequestMapping(value = "/peliculas")
@CrossOrigin(origins = "http://localhost:4200")
public class PeliculaRestController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PeliculaRestController.class);

    private final PeliculaBusiness peliculaBusiness;
    private final PeliculaMapper peliculaMapper;

    public PeliculaRestController(PeliculaBusiness peliculaBusiness,
            PeliculaMapper peliculaMapper) {
        this.peliculaBusiness = peliculaBusiness;
        this.peliculaMapper = peliculaMapper;
    }

    @GetMapping
    public ResponseEntity<List<Pelicula>> findMovies(
            @RequestParam(defaultValue = "") String titulo,
            @RequestParam(defaultValue = "") String genero) {
        List<Pelicula> peliculas = peliculaBusiness.findMoviesByTitleOrGenre(titulo, genero);

        if (peliculas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peliculas);
    }

    @PostMapping
    public ResponseEntity<?> createPelicula(
            @Validated @RequestBody PeliculaCreationDTO peliculaDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        Pelicula pelicula = peliculaMapper.toPelicula(peliculaDTO);
        try {
            Pelicula peliculaGuardada = peliculaBusiness.save(pelicula);
            return ResponseEntity.status(HttpStatus.CREATED).body(peliculaGuardada);
        } catch (SQLException exception) {
            LOGGER.error("No se pudo guardar la película '{}'", peliculaDTO.getTitulo(),
                    exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudo guardar la película.");
        }
    }

}
