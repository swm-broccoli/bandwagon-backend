package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.User;
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

    @Transactional
    public void addGenreToUser(String email, Long genreId) throws Exception{
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("User does not exist!");
        }
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre == null) {
            throw new Exception("Genre does not exist!");
        }
        if (user.getGenres().contains(genre)) {
            log.info("User already has genre: {}", genre.getGenre());
            return;
        }
        user.addGenre(genre);
    }

    @Transactional
    public void deleteGenreFromUser(String email, Long genreId) throws Exception {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("User does not exist!");
        }
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre == null) {
            throw new Exception("Genre does not exist!");
        }
        user.removeGenre(genre);
    }
}
