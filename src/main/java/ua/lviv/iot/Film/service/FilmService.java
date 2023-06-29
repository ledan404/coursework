package ua.lviv.iot.Film.service;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.Film.models.Film;
import ua.lviv.iot.Film.storage.FilmWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Getter
public final class FilmService {

    private final Map<Integer, Film> FilmMap;

    private final FilmWriter filmWriter;

    private final AtomicInteger nextAvailable;

    @Autowired
    public FilmService(final FilmWriter filmWriter) throws IOException {
        this.filmWriter = filmWriter;
        this.nextAvailable = new AtomicInteger(filmWriter.getLastId(FilmWriter.PATH));
        this.FilmMap = filmWriter.getFilmsFromCSV(FilmWriter.PATH);
    }


    public Map<Integer, Film> getMap() {
        return new HashMap<>(FilmMap);
    }

    public List<Film> getAllFilm() {
        return new LinkedList<>(FilmMap.values());
    }

    public Film getFilm(Integer id) {
        return FilmMap.get(id);
    }

    public void postFilm(Film film) throws IOException {
        film.setId(nextAvailable.incrementAndGet());
        FilmMap.put(film.getId(), film);
        filmWriter.writeToCSV(film, FilmWriter.PATH);
    }

    public void putFilm(Integer id, Film film) throws IOException {
        film.setId(id);
        FilmMap.replace(id, film);
        filmWriter.putFilm(film.getId(), film, FilmWriter.PATH);
        filmWriter.writeToCSV(film, FilmWriter.PATH);
    }

    public void deleteFilm(Integer id) throws IOException {
        FilmMap.remove(id);
        filmWriter.deleteFilmBy(id, FilmWriter.PATH);

    }
}
