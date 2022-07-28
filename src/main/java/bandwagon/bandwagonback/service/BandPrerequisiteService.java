package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Area;
import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.Position;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.prerequisite.*;
import bandwagon.bandwagonback.dto.PrerequisiteDto;
import bandwagon.bandwagonback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BandPrerequisiteService {

    private final BandPrerequisiteRepository bandPrerequisiteRepository;
    private final BandPostRepository bandPostRepository;
    private final AreaRepository areaRepository;
    private final GenreRepository genreRepository;
    private final PositionRepository positionRepository;

    @Transactional
    public void addPrerequisite(Long bandId, PrerequisiteDto prerequisiteDto) throws Exception {
        BandPost bandPost = bandPostRepository.findFirstByBand_id(bandId);
        if(bandPost == null) {
            throw new Exception("Band Post does not Exist!");
        }
        switch (prerequisiteDto.getDtype()) {
            case "Age":
                AgePrerequisite agePrerequisite = new AgePrerequisite(prerequisiteDto.getMin(), prerequisiteDto.getMax());
                bandPost.addPrerequisite(agePrerequisite);
                bandPrerequisiteRepository.save(agePrerequisite);
                break;
            case "Area":
                AreaPrerequisite areaPrerequisite = new AreaPrerequisite();
                for (Long id : prerequisiteDto.getIds()) {
                    Area area = areaRepository.findById(id).orElse(null);
                    if (area == null) {
                        throw new Exception("Area does not exist!");
                    }
                    areaPrerequisite.addArea(area);
                }
                bandPost.addPrerequisite(areaPrerequisite);
                bandPrerequisiteRepository.save(areaPrerequisite);
                break;
            case "Gender":
                GenderPrerequisite genderPrerequisite = new GenderPrerequisite(prerequisiteDto.getGender());
                bandPost.addPrerequisite(genderPrerequisite);
                bandPrerequisiteRepository.save(genderPrerequisite);
                break;
            case "Genre":
                GenrePrerequisite genrePrerequisite = new GenrePrerequisite();
                for (Long id : prerequisiteDto.getIds()) {
                    Genre genre = genreRepository.findById(id).orElse(null);
                    if (genre == null) {
                        throw new Exception("Genre does not exist!");
                    }
                    genrePrerequisite.addGenre(genre);
                }
                bandPost.addPrerequisite(genrePrerequisite);
                bandPrerequisiteRepository.save(genrePrerequisite);
                break;
            case "Position":
                PositionPrerequisite positionPrerequisite = new PositionPrerequisite();
                for (Long id : prerequisiteDto.getIds()) {
                    Position position = positionRepository.findById(id).orElse(null);
                    if (position == null) {
                        throw new Exception("Position does not exist!");
                    }
                    positionPrerequisite.addPosition(position);
                }
                bandPost.addPrerequisite(positionPrerequisite);
                bandPrerequisiteRepository.save(positionPrerequisite);
                break;
            default:
                throw new Exception("Wrong dtype for Prerequisite in Request!");
        }
    }
}
