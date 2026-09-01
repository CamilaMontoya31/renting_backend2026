package cr.ac.ucr.paraiso.dsw4.renting.controller.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import cr.ac.ucr.paraiso.dsw4.renting.business.PeliculaBusiness;

@Controller
public class PeliculaController {

    private final PeliculaBusiness peliculaBusiness;

    public PeliculaController(PeliculaBusiness peliculaBusiness) {
        this.peliculaBusiness = peliculaBusiness;
    }

    @RequestMapping(value = "/findMovies", method = RequestMethod.GET )
    public String iniciar(Model model){
        return "findMovies";

    }

    @RequestMapping(value = "/findMovies", method = RequestMethod.POST )
    public String iniciar(Model model, @RequestParam("titulo") String titulo, @RequestParam("genero") String genero){
        model.addAttribute("peliculas", peliculaBusiness.findMoviesByTitleOrGenre(titulo, genero));
        return "findMovies";

    }

}
