package ua.lviv.iot.film.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import ua.lviv.iot.film.models.Film;
import ua.lviv.iot.film.service.FilmService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("films")
public final class FilmController {
    private final FilmService filmService;


    @Autowired
    public FilmController(final FilmService filmService) throws IOException {
        this.filmService = new FilmService(filmService.getFilmWriter());
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        return ResponseEntity.ok(filmService.getAllFilm());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Film> getById(@PathVariable final Integer id) {
        Film film = filmService.getFilm(id);
        if (film != null) {
            return ResponseEntity.ok(film);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Film> create(@RequestBody final Film film) {
        try {
            filmService.postFilm(film);
            return ResponseEntity.ok(film);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Film> put(@RequestBody final Film film,
                                    @PathVariable final Integer id) {
        try {
            filmService.putFilm(id, film);
            return ResponseEntity.ok(film);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Film> delete(@PathVariable final Integer id) {
        if (!filmService.getFilmMap().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        filmService.deleteFilm(id);
        return ResponseEntity.ok().build();
    }

}
