ALTER TABLE film
ADD CONSTRAINT uk_film_title_release_year UNIQUE (title, release_year);