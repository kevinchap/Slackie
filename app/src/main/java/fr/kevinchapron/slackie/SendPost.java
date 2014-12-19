package fr.kevinchapron.Slackie;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by kevinchapron on 18/12/2014.
 */

public class SendPost extends AsyncTask<String, Void, String> {
    public static final String strURL = "https://slack.com/api/chat.postMessage";

    private String token;
    private String channel;
    private String msg;
    private String bot;
    private Context context;
    private LayoutInflater layoutInflater;
    private ViewGroup vError;
    private ViewGroup vValid;

    SendPost(String token, String channel, String msg, String bot, Context context, LayoutInflater layout, ViewGroup vError, ViewGroup vValid){
        this.token = token;
        this.channel = channel;
        this.msg = msg;
        this.bot = bot;
        this.context = context;
        this.layoutInflater = layout;
        this.vError = vError;
        this.vValid = vValid;
    }

    @Override
    protected String doInBackground(String... message) {
        HttpClient httpclient;
        HttpPost request;
        HttpResponse response = null;
        String result = " ";

        // Préparation des paramètres pour la requête
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("channel", channel));
        nameValuePairs.add(new BasicNameValuePair("text", msg));
        nameValuePairs.add(new BasicNameValuePair("username", bot));

        try {
            httpclient = new DefaultHttpClient();
            request = new HttpPost(strURL);
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                result = "true";
            }
        } catch (Exception e) {

        }

        return result;
    }

    protected void onPostExecute(String result) {
        if(result=="true"){
            LayoutInflater inflater = layoutInflater;
            View layout = inflater.inflate(R.layout.validsend_toast, vValid);
            TextView text = (TextView) layout.findViewById(R.id.text_validsend);
            text.setText("Message posted !");
            Toast toast = new Toast(context);
            toast.setView(layout);
            toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }else{
            LayoutInflater inflater = layoutInflater;
            View layout = inflater.inflate(R.layout.errorfields_toast, vError);
            TextView text = (TextView) layout.findViewById(R.id.text_errorfields);
            text.setText("Oops, error. Please try again later.");
            Toast toast = new Toast(context);
            toast.setView(layout);
            toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
