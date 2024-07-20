package growthook.org.bamgang.family.controller;

import growthook.org.bamgang.family.dto.responseDto.FamilyLocationResponseDto;
import growthook.org.bamgang.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
public class FamilyController {

    @Autowired
    FamilyService familyService;

    @GetMapping("/locations/{userId}")
    public List<FamilyLocationResponseDto> getFamilyLocation(@PathVariable("userId") int userId){
        List<FamilyLocationResponseDto> responseDtos = familyService.getFamilyLocation(userId);
        return responseDtos;
    }

}
