package com.ben.content_distribution_api.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ben.content_distribution_api.Film;

/**
 * Factory class for reusable Film query specifications.
 *
 * These predicates are composed by the service layer to support optional
 * search filters without hand-writing SQL.
 */
public class FilmSpecification {

	public static Specification<Film> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                );
    }

    public static Specification<Film> genreEquals(String genre) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("genre")),
                        genre.toLowerCase()
                );
    }

    public static Specification<Film> releaseYearEquals(Integer releaseYear) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("releaseYear"), releaseYear);
    }
}
