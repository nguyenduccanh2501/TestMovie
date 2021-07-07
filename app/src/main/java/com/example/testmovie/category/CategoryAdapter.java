package com.example.testmovie.category;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sun.moviedb_45.R;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.databinding.ItemRecyclerCategoryBinding;
import com.sun.moviedb_45.util.StringUtils;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Movie> mMovies;
    private ItemClickListener mListener;

    public CategoryAdapter(List<Movie> movies, ItemClickListener listener) {
        mMovies = movies;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemRecyclerCategoryBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_recycler_category, viewGroup, false);
        return new ViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(mMovies.get(i));
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    public interface ItemClickListener {
        void onItemClick(Movie movie);
    }
    public void addMovies(List<Movie> movies) {
        int position = mMovies.size();
        mMovies.addAll(movies);
        notifyItemInserted(position);
    }

    public void setMovies(List<Movie> data) {
        if (data != null) {
            mMovies.clear();
            mMovies.addAll(data);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ItemRecyclerCategoryBinding mBinding;
        private ItemClickListener mListener;
        public ObservableField<String> mTitle = new ObservableField<>();
        public ObservableField<String> mReleaseDate = new ObservableField<>();
        public ObservableField<String> mOverView = new ObservableField<>();
        public ObservableField<String> mVoteAverage = new ObservableField();
        public ObservableField<String> mPosterPath = new ObservableField<>();
        private Movie mMovie;

        public ViewHolder(ItemRecyclerCategoryBinding binding, ItemClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(mMovie);
                }
            });
        }

        public void bindData(Movie movie) {
            mMovie = movie;
            if (mBinding.getViewModel() == null) mBinding.setViewModel(this);
            mTitle.set(movie.getTitle());
            mReleaseDate.set(StringUtils.append("Release date: ", movie.getReleaseDate()));
            mOverView.set(movie.getOverview());
            mVoteAverage.set(StringUtils.append("Rating: ", String.valueOf(movie.getVoteAverage())));
            mPosterPath.set(movie.getPosterPath());
        }
    }
}
