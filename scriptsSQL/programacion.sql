CREATE DATABASE movies_db;
USE movies_db;

DROP TABLE IF EXISTS collections;
CREATE TABLE collections(
	id Int,
    name VARCHAR(100),
    poster_path VARCHAR(100),
    backdrop_path VARCHAR(100)
    );
    
SELECT * FROM collections;

DROP TABLE IF EXISTS genres;
CREATE TABLE genres(
	id INT,
    name VARCHAR(100)
    );
    
SELECT * FROM genres;

DROP TABLE IF EXISTS companies;
CREATE TABLE companies(
    name VARCHAR(100),
    id INT
    );
    
SELECT * FROM companies;

DROP TABLE IF EXISTS countries;
CREATE TABLE countries(
	iso_3166_1 VARCHAR(100),
    name VARCHAR(100)
    );
    
SELECT * FROM countries;

DROP TABLE IF EXISTS languages;
CREATE TABLE languages(
	iso_639_1 VARCHAR(100),
    name VARCHAR(100)
    );
    
SELECT * FROM languages;

DROP TABLE IF EXISTS keywords;
CREATE TABLE keywords(
	id Int,
    name VARCHAR(100)
    );
    
SELECT * FROM keywords;

DROP TABLE IF EXISTS casts;
CREATE TABLE casts(
	cast_id INT,
    characte VARCHAR(100),
    credit_id VARCHAR(100),
    gender INT,
    id INT, 
    name VARCHAR(100), 
    orde INT, 
    profile_path VARCHAR(100)
    );
    
SELECT * FROM casts;

DROP TABLE IF EXISTS crew;
CREATE TABLE crew(
	credit_id VARCHAR(100),
    department VARCHAR(100), 
    gender INT, 
    id INT, 
    job VARCHAR(100), 
    name VARCHAR(100), 
    profile_path VARCHAR(100)
    );
    
SELECT * FROM crew;

DROP TABLE IF EXISTS ratings;
CREATE TABLE ratings(
	userId INT,
    rating DOUBLE,
	timestamp DOUBLE PRECISION
    );
    
SELECT * FROM ratings;

DROP TABLE IF EXISTS peliculas;
CREATE TABLE peliculas(
	id INT PRIMARY KEY,
  adult BOOLEAN,
  budget INT,
  homepage VARCHAR(100),
  imdb_id VARCHAR(100),
  original_language VARCHAR(100),
  original_title VARCHAR(100),
  overview TEXT,
  popularity DOUBLE,
  poster_path VARCHAR(100),
  release_date DATE,
  revenue BIGINT,
  runtime INT,
  status VARCHAR(100),
  tagline VARCHAR(255),
  title VARCHAR(100),
  video BOOLEAN,
  vote_average DOUBLE,
  vote_count INT
    );
    
SELECT * FROM peliculas;