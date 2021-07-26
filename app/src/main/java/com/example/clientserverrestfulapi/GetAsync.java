package com.example.clientserverrestfulapi;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class GetAsync extends AsyncTask<String,Void,String> {
    private WeakReference<TextView> safeTitleInfoFromMain;
    private WeakReference<TextView> safeAuthorInfoFromMain;

    public GetAsync(TextView titleInfo, TextView authorInfo){
//        this.titleInfoFromMain = titleInfo;
//        this.authorInfoFromMain = authorInfo;
        this.safeTitleInfoFromMain = new WeakReference<>(titleInfo);
        this.safeAuthorInfoFromMain = new WeakReference<>(authorInfo);
    }

    /**
     *
     * @param strings - String... is an array of Strings of unknown size
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        return ConnectivityHelper.getInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String responseFromBackGround){
        // in case network time-out/other error, JSON parsing will fail and throw an exception
        try{
            /*
            1. Declare a JSON object (main JSON)
            2. Initialize the main JSON with the String response from
            doInBackGround
            3.Declare an array of inner JSONs (Book/Magazine)
             */

            JSONObject mainJson = new JSONObject(responseFromBackGround);
            JSONArray items = mainJson.getJSONArray("items");
            int currentIndex = 0;
            String title = null;
            String authors = null;

            while (currentIndex< items.length() && title == null && authors==null){
                 JSONObject bookMagazine= items.
                         getJSONObject(currentIndex)
                         .getJSONObject("volumeInfo");

                 // this code does NOT take into account an empty field
                try{
                    title = bookMagazine.getString("title");
                    authors = bookMagazine.getString("authors");
                }catch(JSONException e){
                    e.printStackTrace();
                }

                 currentIndex++;
            }

            // either finished iterating over the JSON array or title and authors were found
            if (title!=null & authors!=null){
                safeTitleInfoFromMain.get().setText(title);
                safeAuthorInfoFromMain.get().setText(authors);
            }
            // else if both title and authors were not found
            else{
                safeTitleInfoFromMain.get().setText("No Results were found");
                safeAuthorInfoFromMain.get().setText("");
            }

        }catch(JSONException je){
            // parsing of JSON response failed
            safeTitleInfoFromMain.get().setText("Parsing Failed");
            safeAuthorInfoFromMain.get().setText("");
            je.printStackTrace();
        }
    }

}
