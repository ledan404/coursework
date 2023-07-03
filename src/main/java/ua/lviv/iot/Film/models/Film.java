package ua.lviv.iot.Film.models;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public final class Film {
    private static final String HEADER = "id, name, actor, rating, review, description, fact\n";
    @Id
    public Integer id;

    public String name;

    public List<String> actor;

    public double rating;

    public List<String> review;

    public String description;

    public List<String> fact;

    public Film(Integer id, String name, List<String> actor, double rating, List<String> review,
                String description, List<String> fact) {
        this.id = id;
        this.name = name;
        this.actor = actor;
        this.rating = rating;
        this.review = review;
        this.description = description;
        this.fact = fact;
    }

    public String getHeaders() {
        return HEADER;
    }

    public String toCsv() {
        return id + "," + name + "," + actor + "," + rating + "," + review + "," + description + "," + fact + "\n";
    }
}
