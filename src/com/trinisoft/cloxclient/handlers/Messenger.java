/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinisoft.cloxclient.handlers;

import com.trinisoft.cloxclient.models.Message;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author segun
 */
public class Messenger {
    public static void sendMessage(Message message, Socket socket) {
        String sendThis = "from:" + message.getFrom() + "," +
                "to:" + message.getTo() + "," +
                "date:" + message.getTime().getTime() + "," +
                "msg:" + message.getMsg() + "\n";
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(sendThis);
            writer.flush();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static Message produceMessage(String msg, String from, String to, Date time) {
        Message newMessage = new Message();
        newMessage.setFrom(from);
        newMessage.setTime(time);
        newMessage.setTo(to);
        newMessage.setMsg(msg);                
        return newMessage;
    }
}
