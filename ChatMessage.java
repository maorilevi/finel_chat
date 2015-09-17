package com.example.admin.finel_chat;

/**
 * Created by Admin on 07/09/2015.
 */
public class ChatMessage {
    protected boolean left;
    String message;

    public ChatMessage(Object p0) {
    }
    public ChatMessage (boolean left,String message){

        super();
        this.left=left;
        this.message=message;
    }
}
