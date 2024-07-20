package growthook.org.bamgang.family.controller;

import growthook.org.bamgang.family.dto.requestDto.FamilyLocationRequestDto;
import growthook.org.bamgang.family.dto.responseDto.FamilyLocationResponseDto;
import growthook.org.bamgang.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/locations")
    public void postFamilyLocations(@RequestBody FamilyLocationRequestDto requestDto){
        familyService.postFamilyLocations(requestDto);
    }
}
