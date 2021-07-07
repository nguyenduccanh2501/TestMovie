package com.sun.moviedb_45.ui.actordetail;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.sun.moviedb_45.base.BaseViewModel;
import com.sun.moviedb_45.data.model.Actor;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.data.source.MovieRepository;
import com.sun.moviedb_45.service.response.MovieResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ActorDetailViewModel extends BaseViewModel<ActorDetailNavigator> {
    private static int FIRST_PAGE = 1;
    public ObservableList<Movie> mMovies = new ObservableArrayList<>();
    private int mPage;
    private String mActorKey;
    private MovieRepository mRepository;
    private CompositeDisposable mDisposable;
    private ActorDetailNavigator mNavigator;

    public final ObservableField<String> mName = new ObservableField<>();
    public final ObservableField<String> mAvatar = new ObservableField<>();
    public final ObservableField<String> mBirthday = new ObservableField<>();
    public final ObservableField<String> mPlaceOfBirth = new ObservableField<>();
    public final ObservableField<String> mDepartment = new ObservableField<>();
    public final ObservableField<String> mBiography = new ObservableField<>();

    public ActorDetailViewModel(MovieRepository repository, ActorDetailNavigator navigator) {
        mRepository = repository;
        mNavigator = navigator;
        mDisposable = new CompositeDisposable();
        mPage = FIRST_PAGE;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public String getActorKey() {
        return mActorKey;
    }

    public void setActorKey(String actorKey) {
        mActorKey = actorKey;
    }

    public void getProfile(String actorId){
        Disposable disposable = mRepository.getProfile(actorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Actor>() {
                    @Override
                    public void accept(Actor actor) throws Exception {
                        mAvatar.set(actor.getProfilePath());
                        mName.set(actor.getName());
                        mBirthday.set(actor.getBirthday());
                        mPlaceOfBirth.set(actor.getPlace());
                        mDepartment.set(actor.getDepartment());
                        mBiography.set(actor.getBiography());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        mDisposable.add(disposable);
    }

    public void getMoviesByActor(int page){
        mPage = page;
        Disposable disposable = mRepository.getMoviesByActor(mActorKey, page)
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
