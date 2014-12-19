package fr.kevinchapron.Slackie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePostActivity extends Activity implements View.OnClickListener {

    private String token;
    private EditText titleInputNamePost;
    private EditText inputMsgPost;
    private Spinner inputChannelPost;
    private EditText botInputName;
    private String namePost;
    private String msgPost;
    private String channelPost;
    private String botPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createpost_activity);

        // Fonts
        Typeface LatoBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Bold.ttf");
        Typeface LatoBlack = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Black.ttf");
        Typeface LatoRegular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Regular.ttf");
        TextView nameAppMenuTextViewCreate = (TextView)findViewById(R.id.nameAppMenuTextViewCreate);
        nameAppMenuTextViewCreate.setTypeface(LatoBold);
        TextView titleLabelNamePost = (TextView)findViewById(R.id.titleLabelNamePost);
        titleLabelNamePost.setTypeface(LatoBlack);
        titleInputNamePost = (EditText)findViewById(R.id.titleInputNamePost);
        titleInputNamePost.setTypeface(LatoBold);
        TextView labelMsgPost = (TextView)findViewById(R.id.labelMsgPost);
        labelMsgPost.setTypeface(LatoBlack);
        inputMsgPost = (EditText)findViewById(R.id.inputMsgPost);
        inputMsgPost.setTypeface(LatoBold);
        TextView labelChannelPost = (TextView)findViewById(R.id.labelChannelPost);
        labelChannelPost.setTypeface(LatoBlack);
        TextView botLabelName = (TextView)findViewById(R.id.botLabelName);
        botLabelName.setTypeface(LatoBlack);
        botInputName = (EditText)findViewById(R.id.botInputName);
        botInputName.setTypeface(LatoBold);
        Button buttonSavePost = (Button) findViewById(R.id.buttonSavePost);
        buttonSavePost.setTypeface(LatoBlack);
        Button buttonCloseCreate = (Button) findViewById(R.id.buttonCloseCreate);
        buttonCloseCreate.setTypeface(LatoRegular);

        // Récupération du token
        Intent intent = getIntent();
        if (intent != null) {
            token = intent.getStringExtra("token");
        }

        inputChannelPost = (Spinner)findViewById(R.id.inputChannelPost);
        new ChannelListSlackRequest(token, inputChannelPost, CreatePostActivity.this).execute("");

        // Click sur Save pour créer et enregistrer le trigger
        buttonSavePost.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        namePost = (String)titleInputNamePost.getText().toString();
        msgPost = (String)inputMsgPost.getText().toString();
        channelPost = (String)inputChannelPost.getSelectedItem().toString();
        botPost = (String)botInputName.getText().toString();
        if(TextUtils.isEmpty(namePost) || TextUtils.isEmpty(msgPost) || TextUtils.isEmpty(channelPost) || TextUtils.isEmpty(botPost)) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.errorfields_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
            TextView text = (TextView) layout.findViewById(R.id.text_errorfields);
            text.setText("You must fill in all fields");
            Toast toast = new Toast(getApplicationContext());
            toast.setView(layout);
            toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Intent returnIntent = new Intent();
            returnIntent.putExtra("namePost", namePost);
            returnIntent.putExtra("msgPost", msgPost);
            returnIntent.putExtra("channelPost", channelPost);
            returnIntent.putExtra("botPost", botPost);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    // fonction de post d'un msg (click sur le button Post)
    public void closeCreateHandler(View v){
        finish();
    }
}