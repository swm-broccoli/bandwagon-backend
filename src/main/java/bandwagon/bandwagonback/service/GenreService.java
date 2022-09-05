package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.exception.notfound.BandNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.GenreNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.UserNotFoundException;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandRepository;
import bandwagon.bandwagonback.repository.GenreRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    private final UserRepository userRepository;

    private final BandRepository bandRepository;

    private final BandMemberRepository bandMemberRepository;

    @Transactional
    public void addGenreToUser(String email, Long genreId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre == null) {
            throw new GenreNotFoundException();
        }
        if (user.getGenres().contains(genre)) {
            log.info("User already has genre: {}", genre.getGenre());
            return;
        }
        user.addGenre(genre);
    }

    @Transactional
    public void deleteGenreFromUser(String email, Long genreId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre == null) {
            throw new GenreNotFoundException();
        }
        user.removeGenre(genre);
    }

    @Transactional
    public void addGenreToBand(String email, Long bandId, Long genreId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new BandNotFoundException();
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre == null) {
            throw new GenreNotFoundException();
        }
        if (band.getGenres().contains(genre)) {
            log.info("User already has genre: {}", genre.getGenre());
            return;
        }
        band.addGenre(genre);
    }

    @Transactional
    public void deleteGenreFromBand(String email, Long bandId, Long genreId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new BandNotFoundException();
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre == null) {
            throw new GenreNotFoundException();
        }
        band.removeGenre(genre);
    }
}
