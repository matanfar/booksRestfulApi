package com.example.clientserverrestfulapi;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectivityHelper {

    /*
    GET https://www.googleapis.com/books/v1/volumes?q={search terms}
    OR
    GET https://www.googleapis.com/books/v1/volumes/volumeId

    For example
   GET https://books.googleapis.com/books/v1/volumes?q=alice&maxResults=2&orderBy=newest
     */

    private static final String TAG = ConnectivityHelper.class.getSimpleName();

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY = "q";
    private static final String NUM_RESULT = "maxResults";
    private static final String ORDER = "orderBy";

    static String getInfo(String query){
        HttpURLConnection connection = null;
        BufferedReader JsonDataLines = null;
        String bookAsString = null;

        try{
            // Uri.builder - invoked when using buildUpon method
            Uri uri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(QUERY,query)
                    .appendQueryParameter(NUM_RESULT,"2")
                    .appendQueryParameter(ORDER,"newest")
                    .build();

            URL urlAddress = new URL(uri.toString());
            // openConnection does NOT initiate the connection itself
            connection = (HttpURLConnection) urlAddress.openConnection();
            // define the type of request
            connection.setRequestMethod("GET");
            // actual connection is made using connect method
            connection.connect();

            InputStream dataAsBytes = connection.getInputStream();
            // in order to read the data as a character stream use: InputStreamReader
            InputStreamReader dataAsChars = new InputStreamReader(dataAsBytes);
            JsonDataLines = new BufferedReader(dataAsChars);
            BufferedReader jsonAsLines = new BufferedReader(new InputStreamReader(dataAsBytes));
            StringBuilder responseBuilder = new StringBuilder();

            String singleLine;
            while((singleLine = JsonDataLines.readLine())!=null){
                responseBuilder.append(singleLine);
                responseBuilder.append("\n");
            }
            if (responseBuilder.length()==0){
                // no data was read
                return null;
            }

            // there was data to read
            bookAsString =  responseBuilder.toString();

        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally{
            // close all IO resource demanding objects (streams)
            if (connection!=null)
                connection.disconnect();
            if (JsonDataLines!= null){
                try{
                    JsonDataLines.close();
                }catch(IOException ioe){}
            }
        }

        Log.d(TAG,bookAsString);

        return bookAsString;
    }
}
