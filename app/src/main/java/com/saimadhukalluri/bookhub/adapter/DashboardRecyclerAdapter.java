package com.saimadhukalluri.bookhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saimadhukalluri.bookhub.R;
import com.saimadhukalluri.bookhub.activity.BookDescriptionActivity;
import com.saimadhukalluri.bookhub.model.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder> {
    Context context;
    ArrayList<Book> booksList;

    public DashboardRecyclerAdapter(Context context, ArrayList<Book> booksList) {
        this.context = context;
        this.booksList = booksList;
    }

    static class DashboardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBookName;
        TextView textViewAuthorName;
        TextView textViewBookPrice;
        TextView textViewBookRating;
        ImageView imageViewBookImage;
        LinearLayout ll_listItem_book;

        public DashboardViewHolder(View view) {
            super(view);
            textViewBookName = view.findViewById(R.id.tv_book_name);
            textViewAuthorName = view.findViewById(R.id.tv_bookAuthor_name);
            textViewBookPrice = view.findViewById(R.id.tv_book_price);
            textViewBookRating = view.findViewById(R.id.tv_rating_value);
            imageViewBookImage = view.findViewById(R.id.iv_books_logo);
            ll_listItem_book = view.findViewById(R.id.ll_listItem_book);
        }
    }


    //The below are override and must methods.
    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dashboard_single_row, parent, false);
        return (new DashboardViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
//        holder.imageViewBookImage.setImageResource(booksList.get(position).getBookImage());
        Picasso.get().load(booksList.get(position).getBookImage()).error(R.drawable.default_book_item_image).into(holder.imageViewBookImage);
        holder.textViewBookName.setText(booksList.get(position).getName());
        holder.textViewAuthorName.setText(booksList.get(position).getAuthorName());
        holder.textViewBookPrice.setText(booksList.get(position).getPrise());
        holder.textViewBookRating.setText(booksList.get(position).getRating());
        holder.ll_listItem_book.setOnClickListener(v -> {
            Intent sendingToBookDesc = new Intent(context, BookDescriptionActivity.class);
            sendingToBookDesc.putExtra("bookId", booksList.get(position).getBook_id());
            context.startActivity(sendingToBookDesc);
        });
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }


}
