package com.saimadhukalluri.bookhub.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.saimadhukalluri.bookhub.R;
import com.saimadhukalluri.bookhub.adapter.FavouritesRecyclerAdapter;
import com.saimadhukalluri.bookhub.database.BookDatabase;
import com.saimadhukalluri.bookhub.database.BookEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavouriteFragment extends Fragment {
    RecyclerView rv_favourites;
    RelativeLayout progress_layout_fragment_fav;
    RecyclerView.LayoutManager layout_manager;
    FavouritesRecyclerAdapter fav_adapter;
    List<BookEntity> dbListOfBookEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        rv_favourites = view.findViewById(R.id.rv_favourites);
        progress_layout_fragment_fav = view.findViewById(R.id.progress_layout_fragment_fav);
        layout_manager = new GridLayoutManager(getActivity(), 2);

        try {
            dbListOfBookEntity = new GetDataFromDB(getActivity()).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (dbListOfBookEntity != null && getActivity() != null) {
            progress_layout_fragment_fav.setVisibility(View.GONE);
            fav_adapter = new FavouritesRecyclerAdapter(getActivity(), dbListOfBookEntity);
            rv_favourites.setAdapter(fav_adapter);
            rv_favourites.setLayoutManager(layout_manager);
        }

        return view;
    }

    class GetDataFromDB extends AsyncTask<Void, Void, List<BookEntity>> {
        Context context;

        public GetDataFromDB(Context context) {
            this.context = context;
        }

        @Override
        protected List<BookEntity> doInBackground(Void... voids) {
            BookDatabase db = Room.databaseBuilder(context, BookDatabase.class, "books-db").build();
            return (db.bookDao().getAllBooks());
        }

    }
}