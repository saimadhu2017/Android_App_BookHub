package com.saimadhukalluri.bookhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saimadhukalluri.bookhub.R;
import com.saimadhukalluri.bookhub.database.BookEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouritesRecyclerAdapter extends RecyclerView.Adapter<FavouritesRecyclerAdapter.FavViewHolder> {
    Context context;
    List<BookEntity> bookEntity;

    public FavouritesRecyclerAdapter(Context context, List<BookEntity> bookEntity) {
        this.context = context;
        this.bookEntity = bookEntity;
    }

    static class FavViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_book_recycler_fav;
        TextView tv_bookName_recycler_fav;
        TextView tv_authorName_recycler_fav;
        TextView tv_bookPrice_recycler_fav;
        TextView tv_bookRating_recycler_fav;

        public FavViewHolder(@NonNull View view) {
            super(view);
            iv_book_recycler_fav = view.findViewById(R.id.iv_book_recycler_fav);
            tv_bookName_recycler_fav = view.findViewById(R.id.tv_bookName_recycler_fav);
            tv_authorName_recycler_fav = view.findViewById(R.id.tv_authorName_recycler_fav);
            tv_bookPrice_recycler_fav = view.findViewById(R.id.tv_bookPrice_recycler_fav);
            tv_bookRating_recycler_fav = view.findViewById(R.id.tv_bookRating_recycler_fav);
        }
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_favorite_single_row, parent, false);
        return (new FavViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        BookEntity bookEntityObject = bookEntity.get(position);
        holder.tv_bookName_recycler_fav.setText(bookEntityObject.getBook_name());
        holder.tv_authorName_recycler_fav.setText(bookEntityObject.getBook_author_name());
        holder.tv_bookPrice_recycler_fav.setText(bookEntityObject.getBook_price());
        holder.tv_bookRating_recycler_fav.setText(bookEntityObject.getBook_rating());
        Picasso.get().load(bookEntityObject.getBook_image()).error(R.drawable.default_book_cover).into(holder.iv_book_recycler_fav);
    }

    @Override
    public int getItemCount() {
        return bookEntity.size();
    }
}