package com.example.testmovie.category;

import com.sun.moviedb_45.data.model.Movie;

public interface CategoryNavigator {
    void showMovieDetail(Movie movie);
    void hideLoadMore(boolean hide);
}
