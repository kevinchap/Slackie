package fr.kevinchapron.Slackie;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by kevinchapron on 17/12/2014.
 */

public class ConnectSlackRequest extends AsyncTask<String, Void, String> {
    private ArrayList<NameValuePair> params;
    private String urlRequest;
    //private Intent intent;
    WeakReference<Activity> activityReferenceStart;

    ConnectSlackRequest(ArrayList<NameValuePair> params, Activity activityStart, String url){
        this.params = params;
        //this.intent = intent;
        this.activityReferenceStart = new WeakReference<Activity>(activityStart);
        this.urlRequest = url;
    }

    @Override
    protected String doInBackground(String... message) {
        HttpClient httpclient;
        HttpPost request;
        HttpResponse response = null;
        String result = "";

        try {
            httpclient = new DefaultHttpClient();
            request = new HttpPost(urlRequest);
            request.setEntity(new UrlEncodedFormEntity(this.params));
            response = httpclient.execute(request);
        }
        catch (Exception e) {
            result = "error1";
        }

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null)
            {
                result = result + line ;
            }
        } catch (Exception e) {
            result = "error2";
        }
        //httpEntity = response.getEntity();
        //result = EntityUtils.toString(httpEntity);
        return result;
    }

    protected void onPostExecute(String result)  {
        try {
            JSONObject jsonResult = new JSONObject(result);
            boolean ok = jsonResult.getBoolean("ok");
            if(ok==true){
                String token = jsonResult.getString("access_token");
                if (activityReferenceStart.get() != null) {
                    Activity activityS = activityReferenceStart.get();
                    Intent intent = new Intent(activityS, ListPostActivity.class);
                    intent.putExtra("token", token);
                    activityS.startActivity(intent);
                }else{
                    Activity activityS = activityReferenceStart.get();
                    Intent intent = new Intent(activityS, MainActivity.class);
                    activityS.startActivity(intent);
                }
            }
        } catch (Exception e) {
            Activity activityS = activityReferenceStart.get();
            Intent intent = new Intent(activityS, MainActivity.class);
            activityS.startActivity(intent);
        }
    }
}