package ua.lviv.iot.film.service;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.film.models.Film;
import ua.lviv.iot.film.storage.FilmWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Getter
public final class FilmService {

    private final Map<Integer, Film> filmMap;

    private final FilmWriter filmWriter;

    private final AtomicInteger nextAvailable;

    @Autowired
    public FilmService(final FilmWriter filmWriter) throws IOException {
        this.filmWriter = filmWriter;
        this.nextAvailable = new AtomicInteger(0);
        this.filmMap = new HashMap<>(filmWriter.readFilmsFromCsv());
    }

    public Map<Integer, Film> getFilmMap() {
        return new HashMap<>(filmMap);
    }

    public int getNextAvailable() {
        return nextAvailable.get();
    }


    public Map<Integer, Film> getMap() {
        return new HashMap<>(filmMap);
    }

    public List<Film> getAllFilm() {
        return new LinkedList<>(filmMap.values());
    }

    public Film getFilm(final Integer id) {
        return filmMap.get(id);
    }

    public void postFilm(final Film film) throws IOException {
        film.setId(nextAvailable.incrementAndGet());
        filmMap.put(film.getId(), film);
        filmWriter.getFilmsToCsv(filmMap);
    }

    public void putFilm(final Integer id, final Film film) {
        film.setId(id);
        filmMap.replace(id, film);
        filmWriter.getFilmsToCsv(filmMap);
    }

    public void deleteFilm(final Integer id) {
        filmMap.remove(id);
        filmWriter.getFilmsToCsv(filmMap);
    }
}
