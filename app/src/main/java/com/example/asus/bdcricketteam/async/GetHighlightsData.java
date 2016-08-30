package com.example.asus.bdcricketteam.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.asus.bdcricketteam.database.Database;
import com.example.asus.bdcricketteam.datamodel.HighlightsDataModel;
import com.example.asus.bdcricketteam.prefmanager.OnPreferenceManager;
import com.example.asus.bdcricketteam.security.SecureProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//import android.util.Log;

/**
 * Created by ASUS on 4/30/2016.
 */
public class GetHighlightsData extends AsyncTask<Void, Void, String> {

    private Context mContext;
   // private UIRefreshCallBack mUiRefreshCallBack;
    public String newsFileURL2 = "https://drive.google.com/uc?export=download&id=0B85b1FRNOEQwdmhxa0F2YWFUYWM";
    //https://drive.google.com/file/d/0B85b1FRNOEQwdmhxa0F2YWFUYWM/view?usp=sharing
    public GetHighlightsData(Context context) {
        mContext = context;
    }


    protected String doInBackground(Void... arg0) {
        try {
            // Create a URL for the desired page
            URL url = new URL(newsFileURL2);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            //if (count == 0) {

            //}
            StringBuilder total = new StringBuilder();

            while ((str = in.readLine()) != null) {
                total.append(str);
            }
           // Log.e("string total", total.toString());
            parseJSONGetData(total.toString());
            in.close();
            return total.toString().trim();

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
        return "";
    }

    public void parseJSONGetData(String total) {
        try {
            JSONObject reader = new JSONObject(total);
            JSONObject info = reader.getJSONObject("info");
            int numberOfNews = info.getInt("numberofitems");
            int updateNews = info.getInt("update");
            if (OnPreferenceManager.getInstance(mContext).getHighlightsUpdate() == updateNews) {
                return;
            }
            OnPreferenceManager.getInstance(mContext).setHighlightsUpdate(updateNews);
            // Database.init(SplashScreen.this);

            if (numberOfNews < 1) {
                return;
            }

            Database.init(mContext);
            Database.deleteAll(Database.HIGHLIGHTS_TABLE);
            for (int i = 1; i <= numberOfNews; i++) {
                JSONObject news1 = reader.getJSONObject("item" + i);
                parseJSONInsertDatabase(news1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseJSONInsertDatabase(JSONObject news) {
        String title, detail, imageLink;
        HighlightsDataModel model = new HighlightsDataModel();

        Database.init(mContext);
        //Database.deleteAll(Database.NEWS_TABLE);
        try {
            title = news.getString("title");
            model.setTitle(SecureProcessor.onEncrypt(title.trim()));
            detail = news.getString("duration");
            model.setDuration(SecureProcessor.onEncrypt(detail.trim()));
            imageLink = news.getString("link");
            model.setLink(imageLink);
            Database.insertHighlightsValues(model);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected void onPostExecute(String result) {
       /* mProgressBar.setProgress(30);
        //setRecyclerView();
        //finish();
        //startActivity(new Intent(SplashScreen.this, MainActivity.class));
        new GetSchedule().execute();*/
        /*if (mUiRefreshCallBack != null) {
            mUiRefreshCallBack.onProgress(100);
        }*/


    }
}
