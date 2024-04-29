package growthook.org.bamgang.members.service;

import growthook.org.bamgang.members.domain.FinishedWalk;
import growthook.org.bamgang.members.repository.DataFinishedWalkRepository;

import java.util.ArrayList;
import java.util.List;

public class FinishedWalkService {
    private DataFinishedWalkRepository finishedWalkRepository;

    public FinishedWalkService(DataFinishedWalkRepository finishedWalkRepository) {
        this.finishedWalkRepository = finishedWalkRepository;
    }

    public List<FinishedWalk> findAllById(int id){
        return finishedWalkRepository.findAllByUserId(id);
    }
}
