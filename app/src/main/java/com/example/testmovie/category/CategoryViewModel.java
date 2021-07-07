package com.example.testmovie.category;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.sun.moviedb_45.base.BaseViewModel;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.data.source.MovieRepository;
import com.sun.moviedb_45.service.response.MovieResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CategoryViewModel extends BaseViewModel<CategoryNavigator> {
    private static final int FIRST_PAGE = 1;
    public ObservableList<Movie> mMovies = new ObservableArrayList<>();
    private int mPage;
    private String mCategoryKey;
    private String mGenreKey;
    private String mCompanyKey;
    private MovieRepository mRepository;
    private CompositeDisposable mDisposable;
    private CategoryNavigator mNavigator;

    public CategoryViewModel(MovieRepository repository, CategoryNavigator navigator) {
        mRepository = repository;
        mNavigator = navigator;
        mDisposable = new CompositeDisposable();
        mPage = FIRST_PAGE;
    }

    public String getCategoryKey() {
        return mCategoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        mCategoryKey = categoryKey;
    }

    public String getGenreKey() {
        return mGenreKey;
    }

    public void setGenreKey(String genreKey) {
        mGenreKey = genreKey;
    }

    public String getCompanyKey() {
        return mCompanyKey;
    }

    public void setCompanyKey(String companyKey) {
        mCompanyKey = companyKey;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public void loadCategoriesMovie(int page){
        mPage = page;
        Disposable disposable = mRepository.getMovieByCategory(mCategoryKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        mMovies.clear();
                        mMovies.addAll(movieResponse.getResults());
                        mNavigator.hideLoadMore(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        mDisposable.add(disposable);
    }

    public void loadMoviesByGenre(int page){
        mPage = page;
        Disposable disposable = mRepository.getMovieByGenre(mGenreKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        mMovies.clear();
                        mMovies.addAll(movieResponse.getResults());
                        mNavigator.hideLoadMore(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        mDisposable.add(disposable);
    }

    public void loadMoviesByCompany(int page){
        Disposable disposable = mRepository.getMoviesByCompany(mCompanyKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        mMovies.clear();
                        mMovies.addAll(movieResponse.getResults());
                        mNavigator.hideLoadMore(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        mDisposable.add(disposable);
    }

    public void dispose(){
        mDisposable.dispose();
    }
}
