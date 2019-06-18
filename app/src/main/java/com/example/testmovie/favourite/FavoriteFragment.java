package com.example.testmovie.favourite;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.sun.moviedb_45.BR;
import com.sun.moviedb_45.R;
import com.sun.moviedb_45.base.BaseFragment;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.data.source.MovieRepository;
import com.sun.moviedb_45.data.source.local.MovieLocalDataSource;
import com.sun.moviedb_45.data.source.remote.MovieRemoteDataSource;
import com.sun.moviedb_45.databinding.FragmentFavouriteBinding;
import com.sun.moviedb_45.ui.moviedetail.MovieDetailActivity;

import java.util.ArrayList;

public class FavoriteFragment extends BaseFragment<FragmentFavouriteBinding, FavoriteViewModel>
        implements FavoriteNavigator, FavoriteAdapter.MovieClickListener {
    private FragmentFavouriteBinding mBinding;
    private FavoriteAdapter mAdapter;
    private FavoriteViewModel mViewModel;
    public static FavoriteFragment getInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        initRecycler();
    }

    private void initRecycler() {
        mAdapter = new FavoriteAdapter(new ArrayList<Movie>(), this);
        mBinding.recyclerFavorite.setAdapter(mAdapter);
        mViewModel.loadFavoriteMovies();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadFavoriteMovies();
    }

    @Override
    public void onDestroy() {
        mViewModel.dispose();
        super.onDestroy();
    }

    @Override
    protected FavoriteViewModel getViewModel() {
        if (mViewModel == null) {
            mViewModel = new FavoriteViewModel(MovieRepository.getInstance(
                    MovieRemoteDataSource.getInstance(getActivity()),
                    MovieLocalDataSource.getInstance(getActivity())
            ));
        }
        return mViewModel;
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_favourite;
    }

    @Override
    public void showMovieDetail(Movie movie) {
        startActivity(MovieDetailActivity.getIntent(getActivity(), movie.getId(), movie.getTitle()));
    }

    @Override
    public void showMovie(Movie movie) {
        showMovieDetail(movie);
    }

    @Override
    public void deleteMovie(final Movie movie, final int position) {
        mViewModel.deleteFavoriteMovie(movie);
        mAdapter.removeItem(position);
        Snackbar snackbar = Snackbar
                .make(getView(), "Movie was removed from favorite!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.restoreMovie(movie, position);
                mBinding.recyclerFavorite.scrollToPosition(position);
                mViewModel.addFavoriteMovie(movie);
            }
        });

        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }
}
