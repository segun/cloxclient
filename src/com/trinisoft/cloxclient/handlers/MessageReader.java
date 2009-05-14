/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.cloxclient.handlers;

import com.trinisoft.cloxclient.models.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author segun
 */
public class MessageReader extends Thread {

    Socket socket;

    public MessageReader(Socket socket) {
        this.socket = socket;
    }

    private Message parseMessage(String clientMessage) {
        Message message = new Message();
        String[] splitted = clientMessage.split(",");
        for (String token : splitted) {
            if (token.contains("from")) {
                message.setFrom(token.substring((token.indexOf(":") + 1), token.length()));
            }
            if (token.contains("to")) {
                message.setTo(token.substring((token.indexOf(":") + 1), token.length()));
            }
            if (token.contains("date")) {
                Long dateInMillis = Long.parseLong(token.substring((token.indexOf(":") + 1), token.length()));
                Date date = new Date(dateInMillis);
                message.setTime(date);
            }
            if (token.contains("msg")) {
                message.setMsg(token.substring((token.indexOf(":") + 1), token.length()));
            }
        }
        return message;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = reader.readLine();
                if (message != null) {
                    if (message.startsWith("message")) {
                        message = message.replace("message:", "");
                        System.out.println(parseMessage(message));
                    }
                } else {
                    break;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                break;
            }
        }
    }
}
