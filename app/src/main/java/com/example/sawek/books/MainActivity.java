package com.example.sawek.books;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText editQuery;
    private Button search;
    private JSONAdapter mAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog mDialog;

    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new JSONAdapter(this, getLayoutInflater());
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        editQuery = findViewById(R.id.enter_query);
        search  = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryBooks (editQuery.getText().toString());
            }
        });

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Searching for Book");
        mDialog.setCancelable(false);
    }

    private void queryBooks(String searchString) {

        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        mDialog.show();
        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                        mAdapter.updateData(jsonObject.optJSONArray("docs"));
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        Log.e("failed ", statusCode + " " + throwable.getMessage());
                    }
                });
    }

}
