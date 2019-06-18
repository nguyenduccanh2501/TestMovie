package com.sun.moviedb_45.ui.actordetail;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sun.moviedb_45.R;
import com.sun.moviedb_45.data.model.Movie;
import com.sun.moviedb_45.databinding.ItemActorMovieBinding;
import com.sun.moviedb_45.ui.home.MovieViewModel;

import java.util.List;

public class ActorDetailAdapter extends RecyclerView.Adapter<ActorDetailAdapter.ViewHolder> {
    private List<Movie> mMovies;
    private MovieListener mListener;

    public ActorDetailAdapter(List<Movie> movies, MovieListener listener) {
        mMovies = movies;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemActorMovieBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_actor_movie, viewGroup, false);
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

    public void setMovies(List<Movie> movies) {
        int position = mMovies.size();
        mMovies.addAll(movies);
        notifyItemInserted(position);
    }

    public interface MovieListener {
        void onMovieClick(Movie movie);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemActorMovieBinding mMovieBinding;
        private MovieListener mListener;

        public ViewHolder(ItemActorMovieBinding binding, MovieListener listener) {
            super(binding.getRoot());
            mMovieBinding = binding;
            mListener = listener;
        }

        public void bindData(Movie movie){
            mMovieBinding.setViewModel(new MovieViewModel());
            mMovieBinding.getViewModel().setMovie(movie);
            mMovieBinding.itemMovie.setOnClickListener(this);
            mMovieBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                mListener.onMovieClick(mMovieBinding.getViewModel().getMovie());
            }
        }
    }
}
