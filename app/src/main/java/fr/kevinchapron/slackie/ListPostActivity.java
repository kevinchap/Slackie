package fr.kevinchapron.Slackie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListPostActivity extends Activity implements View.OnClickListener {

    private String token;
    private TriggersAdapter myTriggersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listpost_activity);

        // Fonts
        // Menu
        Typeface LatoBlack = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Black.ttf");
        Typeface LatoBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Bold.ttf");
        Typeface LatoRegular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Regular.ttf");
        Typeface LatoItalic = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Lato-Italic.ttf");
        TextView nameAppMenuTextView = (TextView)findViewById(R.id.nameAppMenuTextView);
        nameAppMenuTextView.setTypeface(LatoBold);
        Button buttonAddMsg = (Button)findViewById(R.id.buttonAddMsg);
        buttonAddMsg.setTypeface(LatoBlack);

        // Récupération du token
        Intent intent = getIntent();
        if (intent != null) {
            token = (String) intent.getStringExtra("token");
        }

        // Ajout d'un trigger (redirect sur CreateActivity)
        buttonAddMsg.setOnClickListener(this);

        final ListView triggersListView = (ListView)findViewById(R.id.postListView);
        myTriggersAdapter = new TriggersAdapter(this, LatoBlack, LatoBold, LatoRegular, LatoItalic, token);
        triggersListView.setAdapter(myTriggersAdapter);

        // long clique sur un item pour supprimer le supprimer (une boîte de dialog s'ouvre)
        triggersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {
                openAlert(index, myTriggersAdapter);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ListPostActivity.this, CreatePostActivity.class);
        intent.putExtra("token", token);
        startActivityForResult(intent, 1);
    }

    // Retour de la vue CreateActivity avec les params
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case (1):
            {
                if(resultCode == RESULT_OK){
                    String namePost=data.getStringExtra("namePost");
                    String msgPost=data.getStringExtra("msgPost");
                    String channelPost=data.getStringExtra("channelPost");
                    String botPost=data.getStringExtra("botPost");
                    Trigger aTrigger = new Trigger(namePost,msgPost,channelPost,botPost);
                    myTriggersAdapter.addTrigger(aTrigger);
                }
                //if (resultCode == RESULT_CANCELED) {

                //}
            }
            break;

            case (10):
            {
                if(resultCode == RESULT_OK){
                    String namePost=data.getStringExtra("namePost");
                    String msgPost=data.getStringExtra("msgPost");
                    String channelPost=data.getStringExtra("channelPost");
                    String botPost=data.getStringExtra("botPost");
                    int position=Integer.parseInt(data.getStringExtra("position"));
                    Trigger bTrigger = new Trigger(namePost,msgPost,channelPost,botPost);

                    myTriggersAdapter.getListTriggers().remove(myTriggersAdapter.getItem(position));
                    myTriggersAdapter.addTriggerInPos(bTrigger, position);
                    //myTriggersAdapter.notifyDataSetChanged();
                }
                //if (resultCode == RESULT_CANCELED) {

                //}
            }
            break;
        }
    }

    // fonction de post d'un msg (click sur le button Post)
    public void sendPostHandler(View v){
        // On récupère le parent du bouton post clické
        LinearLayout vwParentRow = (LinearLayout)v.getParent().getParent();
        // Puis on récupère ses enfants pour avoir le message, le channel et le bot
        TextView msgTextView = (TextView) vwParentRow.findViewById(R.id.msgPostTextView);
        String msg = (String) msgTextView.getText();
        TextView channelTextView = (TextView) vwParentRow.findViewById(R.id.channelPostTextView);
        String allChannelText = (String) channelTextView.getText();
        String channel = allChannelText.substring("will be posted in ".length(),allChannelText.length());
        TextView botTextView = (TextView) vwParentRow.findViewById(R.id.titleBotText);
        String bot = (String) botTextView.getText();
        // On exécute la requête de l'api correspondant à l'envoie d'un msg sur Slack
        ViewGroup vError = (ViewGroup) findViewById(R.id.toast_layout_root);
        ViewGroup vValid = (ViewGroup) findViewById(R.id.toast_layout_valid);
        new SendPost(token, channel, msg, bot, getApplicationContext(), getLayoutInflater(), vError, vValid).execute("");
    }

    // fonction de post d'un msg (click sur le button Post)
    public void editPostHandler(View v){
        // On récupère l'item du bouton post clické
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent().getParent().getParent();
        // On récupère la listView et la position de l'item dans la listeView
        ListView lv = (ListView)v.getParent().getParent().getParent().getParent();
        String position = Integer.toString(lv.getPositionForView(vwParentRow));
        // Puis on récupère ses enfants pour avoir le titre, le message, le channel et le bot
        TextView titleTextView = (TextView) vwParentRow.findViewById(R.id.titlePostTextView);
        String title = (String) titleTextView.getText();
        TextView msgTextView = (TextView) vwParentRow.findViewById(R.id.msgPostTextView);
        String msg = (String) msgTextView.getText();
        //TextView channelTextView = (TextView) vwParentRow.findViewById(R.id.channelPostTextView);
        //String allChannelText = (String) channelTextView.getText();
        //String channel = allChannelText.substring("will be posted in ".length(),allChannelText.length());
        TextView botTextView = (TextView) vwParentRow.findViewById(R.id.titleBotText);
        String bot = (String) botTextView.getText();
        // Lancement et envoie des données à l'activité EditPostActivity
        Intent intent = new Intent(ListPostActivity.this, EditPostActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("title", title);
        intent.putExtra("msg",msg);
        intent.putExtra("bot",bot);
        intent.putExtra("position", position);
        startActivityForResult(intent, 10);
    }

    // fonction permettant d'ouvrir et de personnaliser la boîte de Dialog pour supprimer un trigger
    private void openAlert(int position, TriggersAdapter adapter) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListPostActivity.this);
        alertDialogBuilder.setTitle("Delete this trigger");
        alertDialogBuilder.setMessage("Are you sure ?");
        final int positionToRemove = position;
        final TriggersAdapter myAdapter = adapter;
        // Si oui : suppression du Trigger
        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                myAdapter.getListTriggers().remove(positionToRemove);
                myAdapter.notifyDataSetChanged();
            }
        });
        // Si non : retour sur la liste / on annule
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
                //Toast.makeText(getApplicationContext(), "You chose a negative answer",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
