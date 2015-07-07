package pvr3.tfg.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Pablo on 06/07/2015.
 */
@Controller
@RequestMapping("/map")
public class MapController {

    @RequestMapping(method = RequestMethod.GET)
    public static String showMap(@RequestParam("fileName") String fileName,
                                 @RequestParam("tittle") String tittle,
                                 Model model){
        model.addAttribute("fileName", fileName);
        model.addAttribute("tittle", tittle);
        return "map";
    }

}
