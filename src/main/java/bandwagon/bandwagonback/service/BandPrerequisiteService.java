package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Area;
import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.Position;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.prerequisite.*;
import bandwagon.bandwagonback.dto.PrerequisiteDto;
import bandwagon.bandwagonback.dto.subdto.AreaForm;
import bandwagon.bandwagonback.dto.subdto.IdNameForm;
import bandwagon.bandwagonback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                for (AreaForm areaForm : prerequisiteDto.getAreas()) {
                    Area area = areaRepository.findById(areaForm.getId()).orElse(null);
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
                for (IdNameForm idNameForm : prerequisiteDto.getGenres()) {
                    Genre genre = genreRepository.findById(idNameForm.getId()).orElse(null);
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
                for (IdNameForm idNameForm : prerequisiteDto.getPositions()) {
                    Position position = positionRepository.findById(idNameForm.getId()).orElse(null);
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

    public List<PrerequisiteDto> getAllPrerequisiteByBandId(Long bandId) throws Exception {
        BandPost bandPost = bandPostRepository.findFirstByBand_id(bandId);
        if (bandPost == null) {
            throw new Exception("Band Post does not Exist!");
        }
        List<PrerequisiteDto> res = new ArrayList<>();
        for (BandPrerequisite bandPrerequisite : bandPost.getBandPrerequisites()) {
            switch (bandPrerequisite.getDtype()) {
                case "Age":
                    res.add(new PrerequisiteDto((AgePrerequisite) bandPrerequisite));
                    break;
                case "Area":
                    res.add(new PrerequisiteDto((AreaPrerequisite) bandPrerequisite));
                    break;
                case "Gender":
                    res.add(new PrerequisiteDto((GenderPrerequisite) bandPrerequisite));
                    break;
                case "Genre":
                    res.add(new PrerequisiteDto((GenrePrerequisite) bandPrerequisite));
                    break;
                case "Position":
                    res.add(new PrerequisiteDto((PositionPrerequisite) bandPrerequisite));
                    break;
                default:
                    throw new Exception("Wrong dtype for Prerequisite in DB!");
            }
        }
        return res;
    }

    @Transactional
    public void deletePrerequisite(PrerequisiteDto prerequisiteDto) {
        bandPrerequisiteRepository.deleteById(prerequisiteDto.getId());
    }

    @Transactional
    public void editPrerequisite(Long bandId, PrerequisiteDto prerequisiteDto) throws Exception {
        positionRepository.deleteById(prerequisiteDto.getId());
        addPrerequisite(bandId, prerequisiteDto);
    }
}
