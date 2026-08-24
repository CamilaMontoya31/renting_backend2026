package cr.ac.ucr.paraiso.dsw4.renting.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController

public class HelloRestController {
 @RequestMapping(value="/home", method=RequestMethod.GET)
   public String home(){
       return "Hello World!";
   }

}
