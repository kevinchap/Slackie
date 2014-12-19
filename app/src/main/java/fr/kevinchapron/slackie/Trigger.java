package fr.kevinchapron.Slackie;

/**
 * Created by kevinchapron on 18/12/2014.
 */
public class Trigger {
    String nameTrigger;
    String msgTrigger;
    String channelTrigger;
    String botTrigger;
    public Trigger(String nameTrigger, String msgTrigger, String channelTrigger, String botTrigger) {
        super();
        this.nameTrigger = nameTrigger;
        this.msgTrigger = msgTrigger;
        this.channelTrigger = channelTrigger;
        this.botTrigger = botTrigger;
    }
    public String getNameTrigger() {
        return nameTrigger;
    }
    public String getMsgTrigger() {
        return msgTrigger;
    }
    public String getChannelTrigger(){
        return channelTrigger;
    }
    public String getBotTrigger(){
        return botTrigger;
    }
    public void setNameTrigger(String nameTrigger) {
        this.nameTrigger = nameTrigger;
    }
    public void setMsgTrigger(String msgTrigger) {
        this.msgTrigger = msgTrigger;
    }
    public void setChannelTrigger(String channelTrigger){
        this.channelTrigger = channelTrigger;
    }
    public void setBotTrigger(String botTrigger){
        this.botTrigger = botTrigger;
    }

}