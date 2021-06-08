package com.saimadhukalluri.bookhub.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.saimadhukalluri.bookhub.R;
import com.saimadhukalluri.bookhub.adapter.DashboardRecyclerAdapter;
import com.saimadhukalluri.bookhub.model.Book;
import com.saimadhukalluri.bookhub.util.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {
    RecyclerView rv_dashboard;
    RecyclerView.LayoutManager rv_DashLayoutManager;
    ArrayList<Book> bookInfoList = new ArrayList<>();
    DashboardRecyclerAdapter recyclerAdapter;
    ProgressBar progress_bar;
    RelativeLayout progress_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        setHasOptionsMenu(true);

        progress_bar = view.findViewById(R.id.progress_bar);
        progress_layout = view.findViewById(R.id.progress_layout);

        rv_dashboard = view.findViewById(R.id.rv_dashboard);
        rv_DashLayoutManager = new LinearLayoutManager(getActivity());

        progress_layout.setVisibility(View.VISIBLE);

        if (ConnectionManager.checkConnectivity(getActivity())) {//Internet is there
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String url = "http://13.235.250.119/v1/book/fetch_books";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean status = response.getBoolean("success");
                        if (status) {
                            progress_layout.setVisibility(View.GONE);
                            JSONArray arr = response.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject bookJsonObject = arr.getJSONObject(i);
                                Book tempBook = new Book(
                                        bookJsonObject.getString("book_id"),
                                        bookJsonObject.getString("name"),
                                        bookJsonObject.getString("author"),
                                        bookJsonObject.getString("rating"),
                                        bookJsonObject.getString("price"),
                                        bookJsonObject.getString("image")
                                );
                                bookInfoList.add(tempBook);
                            }
                            recyclerAdapter = new DashboardRecyclerAdapter(getActivity(), bookInfoList);

                            rv_dashboard.setAdapter(recyclerAdapter);
                            rv_dashboard.setLayoutManager(rv_DashLayoutManager);

                        } else {
                            Toast.makeText(getActivity(), "status of server if failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Some Exception occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "server not responding", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-type", "application/json");
                    headers.put("token", "5d11dabdaba175");
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);

        } else {//Internet is not there
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Error");
            dialog.setMessage("There is No Internet Connection");
            dialog.setPositiveButton("Open Settings", (dialog1, which) -> {
                Intent toSettingsOfWifi = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(toSettingsOfWifi);
                getActivity().finish();
            });
            dialog.setNegativeButton("Exit", (dialog12, which) -> {
                ActivityCompat.finishAffinity(getActivity());
            });
            dialog.show();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    //sorting class by using comparator
    class sortByRatings implements Comparator<Book> {
        @Override
        public int compare(Book book1, Book book2) {
            if (book1.getRating().compareToIgnoreCase(book2.getRating()) == 0) {
                return (book1.getName().compareToIgnoreCase(book2.getName()));
            } else {
                return (book1.getRating().compareToIgnoreCase(book2.getRating()));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tool_bar_sort_icon) {//selected item is sort action button or not
            Collections.sort(bookInfoList, new sortByRatings());
            Collections.reverse(bookInfoList);
        }
        recyclerAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
}