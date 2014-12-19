package fr.kevinchapron.Slackie;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by kevinchapron on 18/12/2014.
 */

public class ChannelListSlackRequest extends AsyncTask<String, Void, String>  {
    private String token;
    private Spinner spinner;
    private String urlRequest = "https://slack.com/api/channels.list";
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private JSONArray channels = null;
    Activity activity;
    ArrayList<String> channelList;

    ChannelListSlackRequest(String token, Spinner spinner, Activity activity){
        this.token = token;
        this.spinner = spinner;
        this.activity = activity;
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
            params.add(new BasicNameValuePair("token", token));
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
        try {
            JSONObject jsonResult = new JSONObject(result);
            boolean ok = jsonResult.getBoolean("ok");
            if(ok==true){
                // Getting JSON Array node
                channels = jsonResult.getJSONArray("channels");
                channelList = new ArrayList<String>();
                // Looping through all channels
                for (int i = 0; i < channels.length(); i++) {
                    JSONObject c = channels.getJSONObject(i);
                    channelList.add("#"+c.optString("name"));
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    protected void onPostExecute(String result)  {
        // Set spinner
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(activity,  android.R.layout.simple_spinner_item, channelList);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter_state);
    }
}