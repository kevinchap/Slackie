package fr.kevinchapron.Slackie;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by kevinchapron on 18/12/2014.
 */
public class TriggersAdapter extends BaseAdapter {

    private String token;

    private ArrayList<Trigger> listTriggers;
    private LayoutInflater myLayoutInflater;
    private Typeface LatoBlack;
    private Typeface LatoBold;
    private Typeface LatoRegular;
    private Typeface LatoItalic;

    private String title;
    private String msg;
    private String channel;
    private String bot;

    private TextView textViewMsgTrigger;

    public TriggersAdapter(Context context, Typeface black, Typeface bold, Typeface regular, Typeface italic, String token) {
        listTriggers = new ArrayList<Trigger>();
        myLayoutInflater = LayoutInflater.from(context);
        LatoBlack = black;
        LatoBold = bold;
        LatoRegular = regular;
        LatoItalic = italic;
        this.token = token;
    }

    public void addTrigger(Trigger trigger) {
        listTriggers.add(trigger);
        this.notifyDataSetChanged();
    }
    public void addTriggerInPos(Trigger trigger, int pos){
        listTriggers.add(pos, trigger);
        this.notifyDataSetChanged();
    }

    public ArrayList<Trigger> getListTriggers(){ return listTriggers; }

    @Override
    public int getCount() {
        return listTriggers.size();
    }

    @Override
    public Object getItem(int position) {
        return listTriggers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View viewFromCache, ViewGroup parent) {
        if(null==viewFromCache) {
            viewFromCache = myLayoutInflater.inflate(R.layout.listpost_row,parent,false);
        }
        TextView textViewNameTrigger = (TextView)viewFromCache.findViewById(R.id.titlePostTextView);
        title = listTriggers.get(position).getNameTrigger();
        textViewNameTrigger.setText(title);
        textViewNameTrigger.setTypeface(LatoBlack);
        textViewMsgTrigger = (TextView)viewFromCache.findViewById(R.id.msgPostTextView);
        msg = listTriggers.get(position).getMsgTrigger();
        textViewMsgTrigger.setText(msg);
        textViewMsgTrigger.setTypeface(LatoRegular);
        TextView textViewChannelTrigger = (TextView)viewFromCache.findViewById(R.id.channelPostTextView);
        channel = listTriggers.get(position).getChannelTrigger();
        textViewChannelTrigger.setText("will be posted in "+channel);
        textViewChannelTrigger.setTypeface(LatoItalic);
        TextView textViewBotTrigger = (TextView)viewFromCache.findViewById(R.id.titleBotText);
        bot = listTriggers.get(position).getBotTrigger();
        textViewBotTrigger.setText(bot);
        textViewBotTrigger.setTypeface(LatoBold);

        return viewFromCache;
    }

}
