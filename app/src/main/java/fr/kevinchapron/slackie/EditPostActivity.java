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

/**
 * Created by kevinchapron on 18/12/2014.
 */

public class EditPostActivity extends Activity implements View.OnClickListener {

    private String token;
    private String title;
    private String msg;
    private String bot;
    private String position;
    private EditText titleInputEditName;
    private EditText inputEditMsgPost;
    private Spinner inputEditChannelPost;
    private EditText botEditInputName;
    private String namePost;
    private String msgPost;
    private String channelPost;
    private String botPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpost_activity);

        // Fonts
        Typeface LatoBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Bold.ttf");
        Typeface LatoBlack = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Black.ttf");
        Typeface LatoRegular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Regular.ttf");
        TextView nameAppMenuTextViewEdit = (TextView)findViewById(R.id.nameAppMenuTextViewEdit);
        nameAppMenuTextViewEdit.setTypeface(LatoBold);
        TextView titleEditNamePost = (TextView)findViewById(R.id.titleEditNamePost);
        titleEditNamePost.setTypeface(LatoBlack);
        titleInputEditName = (EditText)findViewById(R.id.titleInputEditName);
        titleInputEditName.setTypeface(LatoBold);
        TextView labelEditMsgPost = (TextView)findViewById(R.id.labelEditMsgPost);
        labelEditMsgPost.setTypeface(LatoBlack);
        inputEditMsgPost = (EditText)findViewById(R.id.inputEditMsgPost);
        inputEditMsgPost.setTypeface(LatoBold);
        TextView labelEditChannelPost = (TextView)findViewById(R.id.labelEditChannelPost);
        labelEditChannelPost.setTypeface(LatoBlack);
        TextView botEditLabelName = (TextView)findViewById(R.id.botEditLabelName);
        botEditLabelName.setTypeface(LatoBlack);
        botEditInputName = (EditText)findViewById(R.id.botEditInputName);
        botEditInputName.setTypeface(LatoBold);
        Button buttonEditPost = (Button) findViewById(R.id.buttonEditPost);
        buttonEditPost.setTypeface(LatoBlack);
        Button buttonCloseEdit = (Button) findViewById(R.id.buttonCloseEdit);
        buttonCloseEdit.setTypeface(LatoRegular);

        // Récupération du token
        Intent intent = getIntent();
        if (intent != null) {
            token = intent.getStringExtra("token");
            title = intent.getStringExtra("title");
            msg = intent.getStringExtra("msg");
            bot = intent.getStringExtra("bot");
            position = intent.getStringExtra("position");
        }

        titleInputEditName.setText(title);
        inputEditMsgPost.setText(msg);
        botEditInputName.setText(bot);

        inputEditChannelPost = (Spinner)findViewById(R.id.inputEditChannelPost);
        new ChannelListSlackRequest(token, inputEditChannelPost, EditPostActivity.this).execute("");

        // Click sur Edit pour éditer le trigger / post
        buttonEditPost.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        namePost = (String)titleInputEditName.getText().toString();
        msgPost = (String)inputEditMsgPost.getText().toString();
        channelPost = (String)inputEditChannelPost.getSelectedItem().toString();
        botPost = (String)botEditInputName.getText().toString();
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
            returnIntent.putExtra("namePost",namePost);
            returnIntent.putExtra("msgPost",msgPost);
            returnIntent.putExtra("channelPost",channelPost);
            returnIntent.putExtra("botPost",botPost);
            returnIntent.putExtra("position", position);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    // fonction de post d'un msg (click sur le button Post)
    public void closeEditHandler(View v){
        finish();
    }
}