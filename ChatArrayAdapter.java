package com.example.admin.finel_chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 07/09/2015.
 */
public class ChatArrayAdapter  extends ArrayAdapter<ChatMessage> {
    private TextView chattext;
    private List<ChatMessage> MessageList = new ArrayList<ChatMessage>();
    private LinearLayout layout;

    public ChatArrayAdapter(Context context, int chat) {
        super(context, chat);
    }

    public ChatArrayAdapter(Context context, int chat, ChatMessage[] objectd) {
        super(context, chat, objectd);
    }

    public void add(ChatMessage object) {
        MessageList.add(object);
        super.add(object);
    }

    public int getCount() {
        return this.MessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.MessageList.get(index);
    }

    public View getView(int position, View ConvertView, ViewGroup perent) {
        View v = ConvertView;
        if (v == null) {
            LayoutInflater infleter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = infleter.inflate(R.layout.chat, perent, false);
        }
        layout = (LinearLayout) v.findViewById(R.id.Messag1);
        ChatMessage Messageobj = getItem(position);
        chattext = (TextView) v.findViewById(R.id.SingeleMessage);

        chattext.setText(Messageobj.message);

        chattext.setBackgroundResource(Messageobj.left ? R.drawable.bubbl_a1 : R.drawable.bubbl_b1);

        layout.setGravity(Messageobj.left ? Gravity.LEFT : Gravity.RIGHT);//message side

        return v;
    }
    public Bitmap decodeToBitmap(byte[] decodedbyte){
        return BitmapFactory.decodeByteArray(decodedbyte, 0, decodedbyte.length);
    }
}
