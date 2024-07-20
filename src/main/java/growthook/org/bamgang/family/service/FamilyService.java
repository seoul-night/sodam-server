package growthook.org.bamgang.family.service;

import growthook.org.bamgang.family.domain.FamilyLocations;
import growthook.org.bamgang.family.dto.responseDto.FamilyLocationResponseDto;
import growthook.org.bamgang.family.repository.FamilyLocationsRepository;
import growthook.org.bamgang.members.domain.Member;
import growthook.org.bamgang.members.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FamilyService {

    @Autowired
    FamilyLocationsRepository familyLocationsRepository;

    @Autowired
    MemberRepository memberRepository;


    public List<FamilyLocationResponseDto> getFamilyLocation(int userId){
        Member user = memberRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException());
        int familyId = user.getFamilyId();
        List<FamilyLocations> locationsList = familyLocationsRepository.findFamilyLocationsByMemberId(familyId);
        List<FamilyLocationResponseDto> responseList = new ArrayList<>();
        for(FamilyLocations loc : locationsList){
            if(!loc.getDate().equals(LocalDate.now())) continue;
            responseList.add(FamilyLocationResponseDto.builder().latitude(loc.getLatitude()).longitude(loc.getLongitude()).build());
        }
        return responseList;
    }
}
