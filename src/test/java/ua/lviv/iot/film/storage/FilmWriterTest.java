package ua.lviv.iot.film.storage;

import org.junit.jupiter.api.Test;
import ua.lviv.iot.film.models.Film;
import ua.lviv.iot.film.service.FilmService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FilmWriterTest {

    @Test
    void getFilmsToCsv() throws IOException {
        FilmWriter filmWriter = new FilmWriter();
        FilmService filmService = new FilmService(filmWriter);
        String expectedFileName = new SimpleDateFormat("'film-'yyyy-MM-dd'.csv'").format(new Date());

        filmWriter.getFilmsToCsv(filmService.getMap());

        File file = new File(expectedFileName);
        assertTrue(file.exists());
    }

    @Test
    void readFilmsFromCsv() throws IOException {
        FilmWriter filmWriter = new FilmWriter();
        FilmService filmService = new FilmService(filmWriter);

        String testFileName = "test-film.csv";
        createTestCsvFile(testFileName);

        Map<Integer, Film> expectedFilmMap = new HashMap<>();
        Film film1 = new Film(1, "Movie Name2", Arrays.asList("Actor 1", "Actor 2"), 4.5, Arrays.asList("Review 1", "Review 2"), "Movie description", Arrays.asList("Fact 1", "Fact 2"));
        expectedFilmMap.put(1, film1);

        Map<Integer, Film> actualFilmMap = filmService.getMap();

        File testFile = new File(testFileName);
        assertTrue(testFile.exists());
        assertEquals(expectedFilmMap.values().toString(), actualFilmMap.values().toString());
        testFile.delete();
    }

    private void createTestCsvFile(String fileName) {
        try {
            File testFile = new File(fileName);
            testFile.createNewFile();

            StringBuilder sb = new StringBuilder();
            sb.append("1,Movie Name2,[Actor 1, Actor 2],4.5,[Review 1, Review 2],Movie description,[Fact 1, Fact 2]\n");

            FileWriter writer = new FileWriter(testFile);
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}