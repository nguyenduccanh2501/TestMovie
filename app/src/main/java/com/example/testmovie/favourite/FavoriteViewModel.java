package com.sun.moviedb_45.ui.favourite;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.sun.moviedb_45.base.BaseViewModel;
import com.sun.moviedb_45.data.model.Actor;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.data.source.MovieRepository;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class FavoriteViewModel extends BaseViewModel<FavoriteNavigator> {
    private MovieRepository mMovieRepository;
    private CompositeDisposable mCompositeDisposable;
    public ObservableList<Movie> favoriteMoviesObservable;

    public FavoriteViewModel(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
        mCompositeDisposable = new CompositeDisposable();
        favoriteMoviesObservable = new ObservableArrayList<>();
    }

    public void loadFavoriteMovies() {
        List<Movie> movies = mMovieRepository.getAllFavoriteMovie();
        favoriteMoviesObservable.clear();
        favoriteMoviesObservable.addAll(movies);
    }

    public boolean deleteFavoriteMovie(Movie movie) {
        return mMovieRepository.deleteFavorite(movie);
    }

    public boolean addFavoriteMovie(Movie movie){
        return mMovieRepository.addFavorite(movie);
    }

    public void dispose() {
        mCompositeDisposable.dispose();
    }
}
