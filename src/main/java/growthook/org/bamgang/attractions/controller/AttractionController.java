package growthook.org.bamgang.attractions.controller;

import growthook.org.bamgang.attractions.dto.response.GetAttractionResponseDto;
import growthook.org.bamgang.attractions.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attractions")
public class AttractionController {

    private final AttractionService attractionService;

    @Autowired
    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping()
    public List<GetAttractionResponseDto> listAll(){
        return attractionService.selectAll();
    }
}
