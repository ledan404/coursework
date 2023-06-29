package ua.lviv.iot.Film.storage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.Film.models.Film;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public final class FilmWriter {

    public static final String PATH = "src/main/java/resources";

    private final SimpleDateFormat fileDateFormat = new SimpleDateFormat("'film-'yyyy-MM-dd'.csv'");

    public Map<Integer, Film> getFilmsFromCSV(String filePath) throws IOException {
        Map<Integer, Film> filmMap = new HashMap<>();
        File folder = new File(filePath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        boolean isFirstLine = true;
                        while ((line = br.readLine()) != null) {
                            if (isFirstLine) {
                                isFirstLine = false;
                                continue; // Skip the header line
                            }
                            String[] values = line.split(",");
                            if (values.length != 7) {
                                // Invalid data row, skip it
                                continue;
                            }
                            try {
                                int id = Integer.parseInt(values[0]);
                                String name = values[1];
                                List<String> actor = Collections.singletonList(values[2]);
                                double rating = Double.parseDouble(values[3]);
                                List<String> review = Collections.singletonList((values[4]));
                                String description = values[5];
                                List<String> fact = Collections.singletonList(values[6]);
                                Film film = new Film(id, name, actor, rating, review, description, fact);
                                filmMap.put(id, film);
                            } catch (NumberFormatException e) {
                                // Invalid numeric data, skip this row
                            }
                        }
                    }
                }
            }
        }
        return filmMap;
    }

    public int getLastId(String filePath) {
        File folder = new File(filePath);
        File[] files = folder.listFiles();
        int lastId = 0;
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        boolean isFirstLine = true;
                        while ((line = br.readLine()) != null) {
                            if (isFirstLine) {
                                isFirstLine = false;
                                continue; // Skip the header line
                            }
                            String[] values = line.split(",");
                            if (values.length > 0) {
                                try {
                                    int id = Integer.parseInt(values[0]);
                                    if (id > lastId) {
                                        lastId = id;
                                    }
                                } catch (NumberFormatException e) {
                                    // Invalid numeric data, skip this row
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return lastId;
    }

    public void writeToCSV(Film film, String filePath) throws IOException {
        String fileName = getFileName(film);
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(getCSVRow(film));
        }
    }

    public void putFilm(Integer id, Film film, String filePath) throws IOException {
        String fileName = getFileName(film);
        File folder = new File(filePath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        boolean isFirstLine = true;
                        List<String> updatedRows = new ArrayList<>();
                        while ((line = br.readLine()) != null) {
                            if (isFirstLine) {
                                isFirstLine = false;
                                updatedRows.add(line); // Add the header line
                                continue;
                            }
                            String[] values = line.split(",");
                            if (values.length > 0) {
                                try {
                                    int fileId = Integer.parseInt(values[0]);
                                    if (fileId == id) {
                                        updatedRows.add(getCSVRow(film));
                                    } else {
                                        updatedRows.add(line);
                                    }
                                } catch (NumberFormatException e) {
                                    // Invalid numeric data, skip this row
                                    updatedRows.add(line);
                                }
                            }
                        }
                        // Rewrite the file with updated rows
                        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                            for (String row : updatedRows) {
                                writer.println(row);
                            }
                        }
                    }
                }
            }
        }
    }

    public void deleteFilmBy(Integer id, String filePath) throws IOException {
        File folder = new File(filePath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        boolean isFirstLine = true;
                        List<String> remainingRows = new ArrayList<>();
                        while ((line = br.readLine()) != null) {
                            if (isFirstLine) {
                                isFirstLine = false;
                                remainingRows.add(line); // Add the header line
                                continue;
                            }
                            String[] values = line.split(",");
                            if (values.length > 0) {
                                try {
                                    int fileId = Integer.parseInt(values[0]);
                                    if (fileId != id) {
                                        remainingRows.add(line);
                                    }
                                } catch (NumberFormatException e) {
                                    // Invalid numeric data, skip this row
                                    remainingRows.add(line);
                                }
                            }
                        }
                        // Rewrite the file with remaining rows
                        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                            for (String row : remainingRows) {
                                writer.println(row);
                            }
                        }
                    }
                }
            }
        }
    }

    private String getFileName(Film film) {
        return fileDateFormat.format(new Date());
    }

    private String getCSVRow(Film film) {
        return film.getId() + "," +
                film.getName() + "," +
                film.getActor() + "," +
                film.getRating() + "," +
                film.getReview() + "," +
                film.getDescription() + "," +
                film.getFact();
    }
}
