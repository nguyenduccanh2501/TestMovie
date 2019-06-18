package com.sun.moviedb_45.ui.category;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sun.moviedb_45.R;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.data.source.MovieRepository;
import com.sun.moviedb_45.data.source.local.MovieLocalDataSource;
import com.sun.moviedb_45.data.source.remote.MovieRemoteDataSource;
import com.sun.moviedb_45.databinding.ActivityCategoryBinding;
import com.sun.moviedb_45.ui.moviedetail.MovieDetailActivity;
import com.sun.moviedb_45.ui.search.SearchActivity;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements CategoryNavigator,
        CategoryAdapter.ItemClickListener {
    public static final String BUNDLE_CATEGORY_KEY = "BUNDLE_CATEGORY_KEY";
    public static final String EXTRA_AGRS = "EXTRA_ARGS";
    public static final String BUNDLE_ACTION_BAR_TITLE = "BUNDLE_ACTION_BAR_TITLE";
    protected String mActionBarTitle;
    protected CategoryViewModel mViewModel;
    protected ActivityCategoryBinding mBinding;
    protected CategoryAdapter mAdapter;

    public static Intent getIntent(Context context, String categoryKey, String categoryTitle) {
        Intent intent = new Intent(context, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_CATEGORY_KEY, categoryKey);
        bundle.putString(BUNDLE_ACTION_BAR_TITLE, categoryTitle);
        intent.putExtra(EXTRA_AGRS, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        initViewModel();
        initActionBar();
        setupRecycler();
    }

    @Override
    protected void onDestroy() {
        mViewModel.dispose();
        super.onDestroy();
    }

    private void setupRecycler() {
        mAdapter = new CategoryAdapter(new ArrayList<Movie>(), this);
        mBinding.recyclerCategoryMovie.setAdapter(mAdapter);
        mBinding.recyclerCategoryMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager)
                        mBinding.recyclerCategoryMovie.getLayoutManager();
                if (manager.findLastCompletelyVisibleItemPosition() == mAdapter.getItemCount() - 1) {
                    loadMoreMovies();
                }
            }
        });
    }

    private void loadMoreMovies() {
        hideLoadMore(false);
        int nextPage = mViewModel.getPage();
        ++nextPage;
        mViewModel.loadCategoriesMovie(nextPage);
    }

    private void initActionBar() {
        setSupportActionBar(mBinding.toolBarCategory);
        getSupportActionBar().setTitle(mActionBarTitle);
        mBinding.toolBarCategory.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mBinding.toolBarCategory.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViewModel() {
        Bundle bundle = getIntent().getBundleExtra(EXTRA_AGRS);
        String categoryKey = bundle.getString(BUNDLE_CATEGORY_KEY);
        mActionBarTitle = bundle.getString(BUNDLE_ACTION_BAR_TITLE);
        mViewModel = new CategoryViewModel(MovieRepository.getInstance(MovieRemoteDataSource.getInstance(this),
                MovieLocalDataSource.getInstance(this)), this);
        mViewModel.setCategoryKey(categoryKey);
        mBinding.setViewModel(mViewModel);
        mViewModel.loadCategoriesMovie(mViewModel.getPage());
    }

    @Override
    public void showMovieDetail(Movie movie) {
        startActivity(MovieDetailActivity.getIntent(this, movie.getId(), movie.getTitle()));
    }

    @Override
    public void hideLoadMore(boolean hide) {
        if (hide) mBinding.progressLoadMore.setVisibility(View.GONE);
        else mBinding.progressLoadMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(Movie movie) {
        showMovieDetail(movie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                startActivity(SearchActivity.getIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
