package ua.lviv.iot.film.storage;


import org.springframework.stereotype.Component;
import ua.lviv.iot.film.models.Film;


import java.io.File;
import java.io.Writer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class FilmWriter {


    private final SimpleDateFormat fileDateFormat = new SimpleDateFormat("'film-'yyyy-MM-dd'.csv'");


    private String getFileName() {
        return fileDateFormat.format(new Date());
    }


    public void getFilmsToCsv(final Map<Integer, Film> filmMap) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(getFileName()), StandardCharsets.UTF_8)) {
            writer.write(Film.getHeaders());
            for (Film film : filmMap.values()) {
//                writer.write(film.getHeaders());
                writer.write(film.toCsv());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Film> readFilmsFromCsv() throws IOException {
        Map<Integer, Film> map = new HashMap<>();
        for (int i = Integer.parseInt(new SimpleDateFormat("dd").format(new Date())) - 1; i >= 0; i--) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -i);
            date = calendar.getTime();
            File file = new File(new SimpleDateFormat("'film-'yyyy-MM-").format(new Date()) + new SimpleDateFormat("dd").format(date) + ".csv");
            if (file.exists()) {
                Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
                scanner.nextLine();
                while (scanner.hasNext()) {
                    String text = scanner.nextLine();
//                    System.out.println(text);
                    Integer id = 0;
                    String name = "";
                    List<String> actors = new LinkedList<>();
                    double rating = 0;
                    List<String> reviews = new LinkedList<>();
                    String description = "";
                    List<String> facts = new LinkedList<>();
                    int groupCounter = 0;
                    Pattern patternLists = Pattern.compile("\\[(.*?)\\]");
                    Matcher matcherLists = patternLists.matcher(text);
                    while (matcherLists.find()) {
                        String listActors = "";
                        String listReviews = "";
                        String listFacts = "";
                        if (groupCounter == 0) {
                            listActors = matcherLists.group();
                            text = text.replace(listActors + ",", "");
                        }
                        if (groupCounter == 1) {
                            listReviews = matcherLists.group();
                            text = text.replace(listReviews + ",", "");
                        }
                        if (groupCounter == 2) {
                            listFacts = matcherLists.group();
                            text = text.replace(listFacts, "");
                        }
                        groupCounter++;
                        Pattern patternArguments = Pattern.compile("[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]\\s]+");
                        Matcher matcherArgumentsActors = patternArguments.matcher(listActors);
                        while (matcherArgumentsActors.find()) {
                            String argument = matcherArgumentsActors.group().trim();
                            actors.add(argument);
                        }
                        Matcher matcherArgumentsReviews = patternArguments.matcher(listReviews);
                        while (matcherArgumentsReviews.find()) {
                            String argument = matcherArgumentsReviews.group().trim();
                            reviews.add(argument);
                        }
                        Matcher matcherArgumentsFacts = patternArguments.matcher(listFacts);
                        while (matcherArgumentsFacts.find()) {
                            String argument = matcherArgumentsFacts.group().trim();
                            facts.add(argument);
                        }
                    }
                    String[] words = text.split(",");
                    id = Integer.valueOf(words[0]);
                    name = words[1];
                    rating = Double.parseDouble(words[2]);
                    description = words[3];
                    Film film = new Film(id, name, actors, rating, reviews, description, facts);
                    map.put(id, film);
                }
            }
        }
        return map;
    }
}
