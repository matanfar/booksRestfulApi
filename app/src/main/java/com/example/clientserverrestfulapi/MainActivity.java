package com.example.clientserverrestfulapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/*
 Our application:
 Google Books RESTful API
We will get a query string from the user we will return the first compatible
result. server will return a JSON and we will parse it, extracting the exact title
and authors

TextView - prompts the user to enter a book name or other text
EditText - this includes an hint to enter a title
Button - opens a network connection, get a JSON, parses it
... RESULTS:
TextView - Full title
TextView - Authors of the corresponding title
 */
public class MainActivity extends AppCompatActivity {
    private EditText queryInput;
    private TextView titleInfo;
    private TextView authorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queryInput = findViewById(R.id.input_id);
        titleInfo = findViewById(R.id.text_title);
        authorInfo = findViewById(R.id.text_author);
    }

    public void getInfoAsync(View view){
        String queryString = queryInput.getText().toString(); // search query from user
        new GetAsync(titleInfo,authorInfo).execute(queryString);

    }


}