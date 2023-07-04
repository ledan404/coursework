package ua.lviv.iot.Film.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.Film.models.Film;
import ua.lviv.iot.Film.service.FilmService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("films")
public final class FilmController {
    private final FilmService filmService;


    @Autowired
    public FilmController(FilmService filmService) throws IOException {
        this.filmService = new FilmService(filmService.getFilmWriter());
    }

    @GetMapping
    public final ResponseEntity<List<Film>> getAll() {
        return ResponseEntity.ok(filmService.getAllFilm());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Film> getById(@PathVariable Integer id) {
        Film film = filmService.getFilm(id);
        if (film != null) {
            return ResponseEntity.ok(film);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Film> create(@RequestBody Film film) {
        try {
            filmService.postFilm(film);
            return ResponseEntity.ok(film);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Film> put(@RequestBody Film film,
                                    @PathVariable Integer id) throws IOException {
        try {
            filmService.putFilm(id, film);
            return ResponseEntity.ok(film);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Film> delete(@PathVariable Integer id) throws IOException {
        if (!filmService.getFilmMap().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        filmService.deleteFilm(id);
        return ResponseEntity.ok().build();
    }

}
