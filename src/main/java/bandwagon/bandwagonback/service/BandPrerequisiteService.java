package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Area;
import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.Position;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.prerequisite.*;
import bandwagon.bandwagonback.dto.PrerequisiteCheckDto;
import bandwagon.bandwagonback.dto.PrerequisiteDto;
import bandwagon.bandwagonback.dto.exception.InvalidTypeException;
import bandwagon.bandwagonback.dto.exception.notfound.*;
import bandwagon.bandwagonback.dto.exception.notof.PrerequisiteNotOfBandException;
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

    private final UserRepository userRepository;
    private final BandPrerequisiteRepository bandPrerequisiteRepository;
    private final BandPostRepository bandPostRepository;
    private final AreaRepository areaRepository;
    private final GenreRepository genreRepository;
    private final PositionRepository positionRepository;

    @Transactional
    public void addPrerequisite(Long postId, PrerequisiteDto prerequisiteDto) {
        BandPost bandPost = bandPostRepository.findById(postId).orElse(null);
        if(bandPost == null) {
            throw new PostNotFoundException();
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
                        throw new AreaNotFoundException();
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
                        throw new GenreNotFoundException();
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
                        throw new PositionNotFoundException();
                    }
                    positionPrerequisite.addPosition(position);
                }
                bandPost.addPrerequisite(positionPrerequisite);
                bandPrerequisiteRepository.save(positionPrerequisite);
                break;
            default:
                throw new InvalidTypeException();
        }
    }

    public List<PrerequisiteDto> getAllPrerequisiteOfPost(Long bandPostId) {
        BandPost bandPost = bandPostRepository.findById(bandPostId).orElse(null);
        if (bandPost == null) {
            throw new PostNotFoundException();
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
                    throw new InvalidTypeException();
            }
        }
        return res;
    }

    @Transactional
    public void deletePrerequisite(Long postId, Long prerequisiteId) {
        BandPrerequisite bandPrerequisite = bandPrerequisiteRepository.findById(prerequisiteId).orElse(null);
        if (bandPrerequisite == null) {
            throw new PrerequisiteNotFoundException();
        }
        if (!bandPrerequisite.getBandPost().getId().equals(postId)) {
            throw new PrerequisiteNotOfBandException();
        }
        bandPrerequisiteRepository.deleteById(prerequisiteId);
    }

    @Transactional
    public void editPrerequisite(Long postId, Long prerequisiteId, PrerequisiteDto prerequisiteDto) {
        deletePrerequisite(postId, prerequisiteId);
        addPrerequisite(postId, prerequisiteDto);
    }

    public List<PrerequisiteCheckDto> checkUserAndReturnForm(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        BandPost bandPost = bandPostRepository.findById(postId).orElse(null);
        if (bandPost == null) {
            throw new PostNotFoundException();
        }
        List<PrerequisiteCheckDto> res = new ArrayList<>();
        for (BandPrerequisite bandPrerequisite : bandPost.getBandPrerequisites()) {
            boolean check = false;
            switch (bandPrerequisite.getDtype()) {
                case "Age":
                    if (((AgePrerequisite) bandPrerequisite).getMin() <= user.getUserAge() && ((AgePrerequisite) bandPrerequisite).getMax() >= user.getUserAge()) {
                        check = true;
                    }
                    res.add(new PrerequisiteCheckDto((AgePrerequisite) bandPrerequisite, check));
                    break;
                case "Area":
                    for (Area area : ((AreaPrerequisite) bandPrerequisite).getAreas()) {
                        if(user.getAreas().contains(area)) {
                            check = true;
                            break;
                        }
                    }
                    res.add(new PrerequisiteCheckDto((AreaPrerequisite) bandPrerequisite, check));
                    break;
                case "Gender":
                    if (user.getGender() == ((GenderPrerequisite) bandPrerequisite).getGender()) {
                        check = true;
                    }
                    res.add(new PrerequisiteCheckDto((GenderPrerequisite) bandPrerequisite, check));
                    break;
                case "Genre":
                    for (Genre genre : ((GenrePrerequisite) bandPrerequisite).getGenres()) {
                        if(user.getGenres().contains(genre)) {
                            check = true;
                            break;
                        }
                    }
                    res.add(new PrerequisiteCheckDto((GenrePrerequisite) bandPrerequisite, check));
                    break;
                case "Position":
                    for (Position position : ((PositionPrerequisite) bandPrerequisite).getPositions()) {
                        if(user.getPositions().contains(position)) {
                            check = true;
                            break;
                        }
                    }
                    res.add(new PrerequisiteCheckDto((PositionPrerequisite) bandPrerequisite, check));
                    break;
                default:
                    throw new InvalidTypeException();
            }
        }
        return res;
    }

    /**
     * 유저 지원 시 지원 가능한지 검증용 메소드
     */
    public Boolean canUserApply(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        BandPost bandPost = bandPostRepository.findById(postId).orElse(null);
        if (bandPost == null) {
            throw new PostNotFoundException();
        }
        for (BandPrerequisite bandPrerequisite : bandPost.getBandPrerequisites()) {
            boolean check = false;
            switch (bandPrerequisite.getDtype()) {
                case "Age":
                    if (((AgePrerequisite) bandPrerequisite).getMin() <= user.getUserAge() && ((AgePrerequisite) bandPrerequisite).getMax() >= user.getUserAge()) {
                        check = true;
                    }
                    break;
                case "Area":
                    for (Area area : ((AreaPrerequisite) bandPrerequisite).getAreas()) {
                        if (user.getAreas().contains(area)) {
                            check = true;
                            break;
                        }
                    }
                    break;
                case "Gender":
                    if (user.getGender() == ((GenderPrerequisite) bandPrerequisite).getGender()) {
                        check = true;
                    }
                    break;
                case "Genre":
                    for (Genre genre : ((GenrePrerequisite) bandPrerequisite).getGenres()) {
                        if (user.getGenres().contains(genre)) {
                            check = true;
                            break;
                        }
                    }
                    break;
                case "Position":
                    for (Position position : ((PositionPrerequisite) bandPrerequisite).getPositions()) {
                        if (user.getPositions().contains(position)) {
                            check = true;
                            break;
                        }
                    }
                    break;
                default:
                    throw new InvalidTypeException();
            }
            if (!check) {
                return false;
            }
        }
        return true;
    }

    /**
     * 밴드 게시글 추천 용 점수 계산
     */
    public Integer calculateScore(User user, BandPost bandPost) {
        Integer score = 0;
        for (BandPrerequisite bandPrerequisite : bandPost.getBandPrerequisites()) {
            switch (bandPrerequisite.getDtype()) {
                case "Age":
                    if (((AgePrerequisite) bandPrerequisite).getMin() <= user.getUserAge() && ((AgePrerequisite) bandPrerequisite).getMax() >= user.getUserAge()) {
                        score++;
                    }
                    break;
                case "Area":
                    for (Area area : ((AreaPrerequisite) bandPrerequisite).getAreas()) {
                        if (user.getAreas().contains(area)) {
                            score++;
                        }
                    }
                    break;
                case "Gender":
                    if (user.getGender() == ((GenderPrerequisite) bandPrerequisite).getGender()) {
                        score++;
                    }
                    break;
                case "Genre":
                    for (Genre genre : ((GenrePrerequisite) bandPrerequisite).getGenres()) {
                        if (user.getGenres().contains(genre)) {
                            score++;
                        }
                    }
                    break;
                case "Position":
                    for (Position position : ((PositionPrerequisite) bandPrerequisite).getPositions()) {
                        if (user.getPositions().contains(position)) {
                            score++;
                        }
                    }
                    break;
                default:
                    throw new InvalidTypeException();
            }
        }
        return score;
    }
}
