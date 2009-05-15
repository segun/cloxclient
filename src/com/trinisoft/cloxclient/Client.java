/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.cloxclient;

import com.trinisoft.cloxclient.handlers.ProtocolHandler;
import com.trinisoft.cloxclient.ui.CloxClient;
import com.trinisoft.libraries.PropertyHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Properties;

/**
 *
 * @author segun
 */
public class Client extends Thread {

    int port;
    String host;
    String username;
    public static boolean STOP_CLIENT = false;
    public Socket clientSocket;
    public CloxClient client;

    public Client(String username, CloxClient client) throws IOException {
        Properties props = new PropertyHelper().getProperties(".\\cloxclient.properties");
        this.port = Integer.parseInt(props.getProperty("com.cloxclient.port"));
        this.host = props.getProperty("com.cloxclient.host");
        this.username = username;
        this.client = client;
        STOP_CLIENT = false;
    }

    @Override
    public void run() {
        //wait until you see a connection
        while (!STOP_CLIENT) {
            try {
                clientSocket = new Socket(host, port);

                String clientDetails = "port:" + clientSocket.getLocalPort() + ",name:" + username + ",address:" + clientSocket.getLocalAddress();
                System.out.println(clientDetails);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                writer.write(clientDetails + "\n");
                writer.flush();

                new ProtocolHandler(clientSocket,client).start();
                break;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        System.out.println(clientSocket.isClosed());
    }
}
