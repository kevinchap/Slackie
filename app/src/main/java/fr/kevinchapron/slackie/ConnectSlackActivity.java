package fr.kevinchapron.Slackie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;

/**
 * Created by kevinchapron on 17/12/2014.
 */
public class ConnectSlackActivity extends Activity {

    private String secretClient;
    private String clientId;
    private FrameLayout myWebContainer;
    private WebView myWebView;
    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private String urlToRequest = "https://slack.com/api/oauth.access";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectslack_activity);

        // Récupération du secretClient et clientId passé en paramètres
        Intent intent = getIntent();
        if (intent != null) {
            secretClient = intent.getStringExtra("secret_client");
            clientId = intent.getStringExtra("client_id");
        }

        // Lancer la web view
        myWebContainer = (FrameLayout) findViewById(R.id.web_container);
        myWebView = (WebView) findViewById(R.id.webview);
        // Traitement sur les urls
        myWebView.setWebViewClient(new WebViewClient( ) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(url.indexOf("code=")!=-1){
                    int posStart = url.indexOf("code=");
                    int posEnd = url.indexOf("&state=");
                    String code = url.substring(posStart+5, posEnd);
                    // Création du tableau de params pour la requète d'authentification
                    params.add(new BasicNameValuePair("client_id", clientId));
                    params.add(new BasicNameValuePair("client_secret", secretClient));
                    params.add(new BasicNameValuePair("code", code));
                    finish();
                    // Lancer la requête pour récupérer le token
                    // avec les trois paramètres nécéssaires à sa récupération
                    // clientId, secretClient et code
                    new ConnectSlackRequest(params, ConnectSlackActivity.this, urlToRequest).execute("");

                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("TAG", "failed: " + failingUrl + ", error code: " + errorCode + " [" + description + "]");
            }

        });
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("https://slack.com/oauth/authorize?client_id="+clientId+"&state=");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        myWebContainer.removeAllViews();
        myWebView.clearHistory();
        myWebView.clearCache(true);
        myWebView.clearView();
        myWebView.destroy();
        myWebView = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
