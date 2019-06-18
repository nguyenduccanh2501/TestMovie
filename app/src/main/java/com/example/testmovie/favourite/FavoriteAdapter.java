package com.example.testmovie.favourite;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sun.moviedb_45.R;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.databinding.ItemRecyclerFavoriteBinding;
import com.sun.moviedb_45.util.StringUtils;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<Movie> mMovies;
    private MovieClickListener mListener;

    public FavoriteAdapter(List<Movie> movies, MovieClickListener listener) {
        mMovies = movies;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemRecyclerFavoriteBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_recycler_favorite, viewGroup, false);
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

    public interface MovieClickListener {
        void showMovie(Movie movie);

        void deleteMovie(Movie movie, int position);
    }

    public void restoreMovie(Movie movie, int position) {
        mMovies.add(position, movie);
        notifyItemInserted(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ObservableField<String> mTitle = new ObservableField<>();
        public ObservableField<String> mReleaseDate = new ObservableField<>();
        public ObservableField<String> mOverView = new ObservableField<>();
        public ObservableField<String> mVoteAverage = new ObservableField();
        public ObservableField<String> mPosterPath = new ObservableField<>();
        private ItemRecyclerFavoriteBinding mBinding;
        private MovieClickListener mListener;
        private Movie mMovie;

        public ViewHolder(ItemRecyclerFavoriteBinding binding, MovieClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
            mBinding.getRoot().setOnClickListener(this);
            mBinding.imageGarbage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener == null) return;
            switch (v.getId()) {
                case R.id.image_garbage:
                    mListener.deleteMovie(mMovie, getAdapterPosition());
                break;
                default:
                    mListener.showMovie(mMovie);
                    break;
            }
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

    public void removeItem(int position) {
        mMovies.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}
