/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.cloxclient;

import com.trinisoft.cloxclient.handlers.ClientsReader;
import com.trinisoft.cloxclient.handlers.MessageReader;
import com.trinisoft.cloxclient.handlers.Messenger;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author segun
 */
public class Client extends Thread {

    int port;
    String host;
    String username;

    public Client(String username) {
        this.port = 1981;
        this.host = "localhost";
        this.username = username;
    }

    @Override
    public void run() {
        //wait until you see a connection        
        while (true) {
            try {
                Socket socket = new Socket(host, port);
                String clientDetails = "port:" + socket.getLocalPort() + ",name:" + username + ",address:" + socket.getLocalAddress();
                System.out.println(clientDetails);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write(clientDetails + "\n");
                writer.flush();

                new ClientsReader(socket).start();
                //Messenger.sendMessage(Messenger.produceMessage("Hello Who's there", username, "dele", new Date()), socket);
                new MessageReader(socket).start();
                break;
            } catch (IOException ioe) {
                //ioe.printStackTrace();
            }
        } 
    }
}
