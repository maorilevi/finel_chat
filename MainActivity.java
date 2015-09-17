package com.example.admin.finel_chat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity {
    private ChatArrayAdapter abp;
    private ListView list;
    private EditText chattext;
    private Button send;


    public TextView wellcomeMessage;
    public String userMessage;
    public Button saveUser;
    public String userNameFromUser;
    public String userGETmessage="PFUCQvdSRs";
    public String userSETmessage="TYPovz7BKs";
    private static Thread mythread;
    private static boolean threadRunning;

    Intent in;
    private boolean side = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(this);//*open referens in this app
        final SharedPreferences.Editor editor = prefernces.edit();
        final String history = prefernces.getString(getString(R.string.pref_user_name1), "no history");



        threadRunning=true;
        mythread=new Thread(){
            public void run(){
                while(threadRunning){
                    try{
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
                        query.getInBackground(userGETmessage, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {
                                    String holdetMessages = parseObject.getString("text");//*geting the message
                                    parseObject.put("text", "");//*clears the fields "text" on parse
                                    parseObject.saveInBackground();//*saving changs
                                    if (!holdetMessages.isEmpty()) {
                                        String[] assis = holdetMessages.split("#split#");
                                        for (int counter = 0; counter < assis.length; counter++) {
                                            if(counter!=0)
                                                if(assis[counter-1].contains(userGETmessage))
                                                    if(assis[counter]!=" ")
                                                        abp.add(new ChatMessage(!side, assis[counter]));
                                        }
                                        if(holdetMessages!=""||holdetMessages!=" ")
                                        {
                                            String holding = prefernces.getString(getString(R.string.pref_user_name1), " ");
                                            SharedPreferences.Editor editor = prefernces.edit();
                                            editor.putString(getString(R.string.pref_user_name1), holding + holdetMessages);
                                            editor.commit();
                                        }
                                    }
                                }
                            }
                        });Thread.sleep(2500);
                    }
                    catch (InterruptedException ex){}
                }
            }
        };mythread.start();

        //parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "PqYz5i7rYEd8qrW7tQwGoXlTY1i3FQjPw6WmXjbt", "1mDWATaC83THnsYZ3sVoAuX7gmiPSIyrUXhjlyE1");
        //parse




        Intent i = getIntent();
        send = (Button) findViewById(R.id.btnsend);
        list = (ListView) findViewById(R.id.listview);
        abp = new ChatArrayAdapter(getApplicationContext(), R.layout.chat);
        chattext = (EditText) findViewById(R.id.chat);
        chattext.setOnClickListener(new View.OnClickListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
            @Override
            public void onClick(View v) {
            }
        });
        //*send message --------------------------------------------------------------
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!chattext.getText().toString().isEmpty())
                {
                    //*update massege in parse
                    userMessage = chattext.getText().toString();
                    sendChatMessage();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
                    query.getInBackground(userSETmessage, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                if(userMessage!=""||userMessage!=" ")
                                {
                                    String holdetMessages=parseObject.getString("text");
                                    parseObject.put("text",holdetMessages+"#split#"+userSETmessage+"#split#"+userMessage);
                                    parseObject.saveInBackground();
                                }
                            }
                        }
                    });
                    //*saving message in phone prefernce
                    String holding = prefernces.getString(getString(R.string.pref_user_name1), " ");
                    SharedPreferences.Editor editor = prefernces.edit();
                    if(userMessage!=""||userMessage!=" "){
                        editor.putString(getString(R.string.pref_user_name1), holding + "#split#"+userSETmessage+"#split#"+ userMessage );
                        editor.commit();
                        Toast toast = Toast.makeText(MainActivity.this, getString(R.string.message_user_saved, userMessage), Toast.LENGTH_SHORT);
                        toast.show(); }
                }
            }
        });
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(abp);
        abp.registerDataSetObserver(new DataSetObserver() {
            public void OnChanged() {
                super.onChanged();
                list.setSelection(abp.getCount() - 1);
            }
        });
        //*open history chat////
        String[] assis = history.split("#split#");
        for (int counter = 0; counter < assis.length; counter++)
                if(counter!=0&&assis[counter]!=" "&&assis[counter]!=null)
                {
                        if(assis[counter-1].contains(userSETmessage))
                            abp.add(new ChatMessage(side, assis[counter]));
                    else
                        if(assis[counter-1].contains(userGETmessage))
                                abp.add(new ChatMessage(!side, assis[counter]));
                }
        }
    private boolean sendChatMessage() {
        abp.add(new ChatMessage(side, chattext.getText().toString()));
        chattext.setText("");
        //*side = !side;
        return true;
    }
}
