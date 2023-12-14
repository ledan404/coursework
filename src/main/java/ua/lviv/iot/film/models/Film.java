package ua.lviv.iot.film.models;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
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
        this.actor = new ArrayList<>(actor);
        this.rating = rating;
        this.review = new ArrayList<>(review);
        this.description = description;
        this.fact = new ArrayList<>(fact);
    }

    public List<String> getActor() {
        return new ArrayList<>(actor);
    }

    public List<String> getFact() {
        return new ArrayList<>(fact);
    }

    public List<String> getReview() {
        return new ArrayList<>(review);
    }


    public static String getHeaders() {
        return HEADER;
    }

    public String toCsv() {
        return id + "," + name + "," + actor + ","
                + rating + "," + review + ","
                + description + "," + fact + "\n";
    }
    public void setActor(final List<String> actor) {
        this.actor = new ArrayList<>(actor);
    }

    public void setFact(final List<String> fact) {
        this.fact = new ArrayList<>(fact);
    }

    public void setReview(final List<String> review) {
        this.review = new ArrayList<>(review);
    }
}