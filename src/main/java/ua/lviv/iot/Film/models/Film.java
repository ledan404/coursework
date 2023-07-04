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
    private static final String
            HEADER = "id, name, actor, rating, review, description, fact\n";
    @Id
    private Integer id;

    private String name;

    private List<String> actor;

    private double rating;

    private List<String> review;

    private String description;

    private List<String> fact;

    public Film(final Integer id,
                final String name,
                final List<String> actor,
                final double rating,
                final List<String> review,
                final String description,
                final List<String> fact) {
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
        return id + "," + name + "," + actor + ","
                + rating + "," + review + ","
                + description + "," + fact + "\n";
    }
}
