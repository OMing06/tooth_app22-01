package com.example.tooset_test02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    EditText searchWard_et;                    //ETSrc, SrcBtn, RecyView
    Button search_btn;
    RecyclerView recyclerView_searchList;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchWard_et = findViewById(R.id.ETSrc);
        search_btn = findViewById(R.id.SrcBtn);
        recyclerView_searchList = findViewById(R.id.RecyView);
        searchAdapter = new SearchAdapter(this);

        LinearLayoutManager recyclerView_layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        recyclerView_searchList.setLayoutManager(recyclerView_layoutManager);
        recyclerView_searchList.setAdapter(searchAdapter);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSearch(String.valueOf(searchWard_et.getText()));
            }
        });
    }

    private void requestSearch(String ward) {
        String query;

        if(SearchModel.requestQueue == null) {
            SearchModel.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        try {
            query = URLEncoder.encode(ward, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            query = null;
            e.printStackTrace();
        }
        Request request = new StringRequest(Request.Method.GET,
                SearchModel.HOST + "?query=" + query + "&display=100&start=2&sort=sim",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-Naver-Client-Id", SearchModel.CLIENT_ID);
                params.put("X-Naver-Client-Secret", SearchModel.CLIENT_SECRET);
                return params;
            }
        };

        request.setShouldCache(false);
        SearchModel.requestQueue.add(request);

    }

    private void processResponse(String response) {
        Gson gson = new Gson();
        SearchModel.SearchItem searchItem = gson.fromJson(response, SearchModel.SearchItem.class);
        if (searchItem != null) {
            searchAdapter.setItems(searchItem.items);
            searchAdapter.notifyDataSetChanged();
        }
    }
}