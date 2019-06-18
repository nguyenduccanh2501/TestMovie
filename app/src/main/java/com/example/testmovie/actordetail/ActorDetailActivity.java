package com.sun.moviedb_45.ui.actordetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sun.moviedb_45.R;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.data.source.MovieRepository;
import com.sun.moviedb_45.data.source.local.MovieLocalDataSource;
import com.sun.moviedb_45.data.source.remote.MovieRemoteDataSource;
import com.sun.moviedb_45.databinding.ActivityActorDetailBinding;
import com.sun.moviedb_45.ui.moviedetail.MovieDetailActivity;

import java.util.ArrayList;

public class ActorDetailActivity extends AppCompatActivity implements
        ActorDetailAdapter.MovieListener, ActorDetailNavigator {
    public static final String BUNDLE_ACTOR_KEY = "BUNDLE_ACTOR_KEY";
    public static final String EXTRA_AGRS = "EXTRA_ARGS";
    public static final String BUNDLE_ACTION_BAR_TITLE = "BUNDLE_ACTION_BAR_TITLE";
    protected String mActionBarTitle;
    protected ActorDetailViewModel mViewModel;
    protected ActivityActorDetailBinding mBinding;
    protected ActorDetailAdapter mAdapter;
    private String mActorName;
    private String mActorId;

    public static Intent getIntent(Context context, String actorKey, String actorTitle) {
        Intent intent = new Intent(context, ActorDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_ACTOR_KEY, actorKey);
        bundle.putString(BUNDLE_ACTION_BAR_TITLE, actorTitle);
        intent.putExtra(EXTRA_AGRS, bundle);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_actor_detail);
        receiveData();
        initToolBar();
        initViewModel();
        setupRecycler();
        mBinding.textPlaceOfBirth.setSelected(true);
    }

    @Override
    protected void onDestroy() {
        mViewModel.dispose();
        super.onDestroy();
    }

    private void setupRecycler() {
        mAdapter = new ActorDetailAdapter(new ArrayList<Movie>(), this);
        mBinding.recyclerCastMovie.setAdapter(mAdapter);
        mBinding.recyclerCastMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager)
                        mBinding.recyclerCastMovie.getLayoutManager();
                if (manager.findLastCompletelyVisibleItemPosition() == mAdapter.getItemCount() - 1) {
                    loadMoreMovies();
                }
            }
        });
    }
    public void receiveData() {
        Bundle bundle = getIntent().getBundleExtra(EXTRA_AGRS);
        mActorId = bundle.getString(BUNDLE_ACTOR_KEY);
        mActorName = bundle.getString(BUNDLE_ACTION_BAR_TITLE);
    }
    private void loadMoreMovies() {
        hideLoadMore(false);
        int nextPage = mViewModel.getPage();
        ++nextPage;
        mViewModel.getMoviesByActor(nextPage);
    }

    private void initToolBar() {
        setSupportActionBar(mBinding.toolBarActor);
        getSupportActionBar().setTitle(mActorName);
        mBinding.toolBarActor.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mBinding.toolBarActor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViewModel() {
        Bundle bundle = getIntent().getBundleExtra(EXTRA_AGRS);
        mActionBarTitle = bundle.getString(BUNDLE_ACTION_BAR_TITLE);
        mViewModel = new ActorDetailViewModel(MovieRepository.getInstance(MovieRemoteDataSource.getInstance(this),
                MovieLocalDataSource.getInstance(this)), this);
        mViewModel.setActorKey(mActorId);
        mBinding.setViewModel(mViewModel);
        mViewModel.getProfile(mActorId);
        mViewModel.getMoviesByActor(mViewModel.getPage());
    }

    @Override
    public void onMovieClick(Movie movie) {
        startActivity(MovieDetailActivity.getIntent(this, movie.getId(), movie.getTitle()));
    }


    @Override
    public void hideLoadMore(boolean hide) {
        if (hide) mBinding.progressLoad.setVisibility(View.GONE);
        else mBinding.progressLoad.setVisibility(View.VISIBLE);
    }
}
