package com.saimadhukalluri.bookhub.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.saimadhukalluri.bookhub.R;
import com.saimadhukalluri.bookhub.database.BookDatabase;
import com.saimadhukalluri.bookhub.database.BookEntity;
import com.saimadhukalluri.bookhub.util.ConnectionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BookDescriptionActivity extends AppCompatActivity {
    ImageView book_image;
    TextView book_name;
    TextView book_author_name;
    TextView book_price;
    TextView book_rating;
    TextView book_desc;
    RelativeLayout progress_layout;
    Button add_to_fav;
    String book_id = "100";//store some random value
    BookEntity bookEntity;
    boolean isBookThere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        book_image = findViewById(R.id.iv_book_desc);
        book_name = findViewById(R.id.tv_book_name_desc);
        book_author_name = findViewById(R.id.tv_book_authorName_desc);
        book_price = findViewById(R.id.tv_book_price_desc);
        book_rating = findViewById(R.id.tv_rating_desc);
        book_desc = findViewById(R.id.tv_book_desc_para);
        progress_layout = findViewById(R.id.progress_layout_for_bookDesc);
        add_to_fav = findViewById(R.id.b_add_to_fav);

        progress_layout.setVisibility(View.VISIBLE);
        add_to_fav.setVisibility(View.GONE);

        Intent intent = getIntent(); //intent We Get After Clicking Book item list
        if (intent != null) {
            book_id = intent.getStringExtra("bookId");
        } else {
            finish();
            Toast.makeText(this, "you are not allowed", Toast.LENGTH_SHORT).show();
        }
        if (book_id.equals("100")) {
            finish();
            Toast.makeText(this, "you are not allowed", Toast.LENGTH_SHORT).show();
        }


        if (ConnectionManager.checkConnectivity(this)) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://13.235.250.119/v1/book/get_book/";

            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("book_id", book_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean status = response.getBoolean("success");
                        if (status) {
                            add_to_fav.setVisibility(View.VISIBLE);
                            progress_layout.setVisibility(View.GONE);
                            JSONObject jsonObject = response.getJSONObject("book_data");
                            Picasso.get().load(jsonObject.getString("image")).error(R.drawable.default_book_cover).into(book_image);
                            book_name.setText(jsonObject.getString("name"));
                            book_author_name.setText(jsonObject.getString("author"));
                            book_price.setText(jsonObject.getString("price"));
                            book_rating.setText(jsonObject.getString("rating"));
                            book_desc.setText(jsonObject.getString("description"));

                            bookEntity = new BookEntity(
                                    Integer.parseInt(jsonObject.getString("book_id")),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("author"),
                                    jsonObject.getString("description"),
                                    jsonObject.getString("price"),
                                    jsonObject.getString("rating"),
                                    jsonObject.getString("image")
                            );
                            AsyncTask<Void, Void, Boolean> checkBookIsThere = new DBAsyncTask(getApplicationContext(), bookEntity, 1).execute();
                            isBookThere = checkBookIsThere.get();
                            System.out.println("output is: 1st operation");
                            if (isBookThere) {
                                add_to_fav.setText("Remove from list");
                            } else {
                                add_to_fav.setText("Add to Fav");
                            }

                            add_to_fav.setOnClickListener(v -> {
                                try {
                                    isBookThere = new DBAsyncTask(getApplicationContext(), bookEntity, 1).execute().get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!isBookThere) {//book will added to db
                                    System.out.println("output is: Button clicked for 2nd");
                                    AsyncTask<Void, Void, Boolean> bookAdded = new DBAsyncTask(getApplicationContext(), bookEntity, 2).execute();
                                    try {
                                        boolean result = bookAdded.get();
                                        System.out.println("output is: 2nd operation");
                                        if (result) {
                                            Toast.makeText(getApplicationContext(), "book added to database", Toast.LENGTH_SHORT).show();
                                            add_to_fav.setText("Remove from list");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "book not added some error came", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                } else {//book will removed from db
                                    System.out.println("output is: Button clicked for 3rd");
                                    AsyncTask<Void, Void, Boolean> bookRemoved = new DBAsyncTask(getApplicationContext(), bookEntity, 3).execute();
                                    try {
                                        boolean result = bookRemoved.get();
                                        System.out.println("output is: 3rd operation");
                                        if (result) {
                                            Toast.makeText(getApplicationContext(), "book removed from db", Toast.LENGTH_SHORT).show();
                                            add_to_fav.setText("Add to Fav");
                                            isBookThere = false;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "book not removed some error came", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "You are not allowed status is failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Some Exception occurred", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "response not got from server", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-type", "application/json");
                    headers.put("token", "5d11dabdaba175");
                    return headers;
                }
            };

            queue.add(jsonObjectRequest);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Error");
            dialog.setMessage("There is No Internet Connection");
            dialog.setPositiveButton("Open Settings", (dialog1, which) -> {
                Intent toSettingsOfWifi = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(toSettingsOfWifi);
                finish();
            });
            dialog.setNegativeButton("Exit", (dialog12, which) -> {
                ActivityCompat.finishAffinity(this);
            });
            dialog.show();
        }


    }

    class DBAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Context context;
        BookEntity bookEntity;
        int modeOfOperation;
        BookDatabase db;

        public DBAsyncTask(Context context, BookEntity bookEntity, int modeOfOperation) {
            this.context = context;
            this.bookEntity = bookEntity;
            this.modeOfOperation = modeOfOperation;
            this.db = Room.databaseBuilder(context, BookDatabase.class, "books-db").build();
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            if (this.modeOfOperation == 1) {
                //check the DB is book added or not
                BookEntity book = db.bookDao().getBookById(this.bookEntity.getBook_id());
                db.close();
                return (book != null);
            } else if (this.modeOfOperation == 2) {
                //add or save the book in table of DB
                db.bookDao().insertBook(this.bookEntity);
                db.close();
                return (true);
            } else if (this.modeOfOperation == 3) {
                //Deleting the book from table of DB
                db.bookDao().deleteBook(this.bookEntity);
                db.close();
                return (true);
            }
            return (false);
        }
    }
}