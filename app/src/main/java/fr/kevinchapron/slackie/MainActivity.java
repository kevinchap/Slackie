package fr.kevinchapron.Slackie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {

    private String secretClient = "6f1138c531419c72261837db241ee47e";
    private String clientId = "3221071404.3243281996";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fonts splashscreen
        Typeface LatoBlack = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Black.ttf");
        Typeface LatoBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Bold.ttf");
        TextView nameApplicationTextView = (TextView)findViewById(R.id.nameApplicationTextView);
        nameApplicationTextView.setTypeface(LatoBlack);
        TextView sloganApplicationTextView = (TextView)findViewById(R.id.sloganApplicationTextView);
        sloganApplicationTextView.setTypeface(LatoBold);
        Button connectSlackButton = (Button)findViewById(R.id.connectSlackButton);
        connectSlackButton.setTypeface(LatoBold);

        // Click pour se connecter à l'api de Slack via la webview
        connectSlackButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // Changement de vue (Webview avec fenêtre de connection à l'appli de slack)
        Intent intent = new Intent(MainActivity.this, ConnectSlackActivity.class);
        // Ajout des identifiants nécessaires à la récupération du code
        intent.putExtra("secret_client", secretClient);
        intent.putExtra("client_id", clientId);
        startActivity(intent);
    }
}
