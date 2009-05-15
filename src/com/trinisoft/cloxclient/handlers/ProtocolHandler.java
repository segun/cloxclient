/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.cloxclient.handlers;

import com.trinisoft.cloxclient.helpers.ClientList;
import com.trinisoft.cloxclient.models.Message;
import com.trinisoft.cloxclient.models.Messages;
import com.trinisoft.cloxclient.ui.CloxClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author segun
 */
public class ProtocolHandler extends Thread {

    Socket socket;
    CloxClient mclient;

    public ProtocolHandler(Socket socket, CloxClient client) {
        this.socket = socket;
        this.mclient = client;
    }

    private Message parseMessage(String clientMessage) {
        Message message = new Message();
        String[] splitted = clientMessage.split(":s");
        for (String token : splitted) {
            if (token.contains("from")) {
                message.setFrom(token.substring((token.indexOf("=") + 1), token.length()));
            }
            if (token.contains("to")) {
                message.setTo(token.substring((token.indexOf("=") + 1), token.length()));
            }
            if (token.contains("date")) {
                Long dateInMillis = Long.parseLong(token.substring((token.indexOf("=") + 1), token.length()));
                Date date = new Date(dateInMillis);
                message.setTime(date);
            }
            if (token.contains("msg")) {
                message.setMsg(token.substring((token.indexOf("=") + 1), token.length()));
            }
        }
        return message;
    }

    private boolean updateClientList(String newClientList) {
        String[] splitted = newClientList.split(",");
        ArrayList<String> clientNames = new ArrayList<String>();
        for (String oneClient : splitted) {
            clientNames.add(oneClient);
        }
        ClientList.clientList = new ArrayList<String>();
        ClientList.clientList = clientNames;
        return true;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String serverMessage = "";
                while ((serverMessage = reader.readLine()) != null) {

                    if (serverMessage != null) {
                        if (serverMessage.startsWith("clients")) {
                            serverMessage = serverMessage.replace("clients:", "");
                            updateClientList(serverMessage);
                            /*UI*/
                            mclient.btnLogin.setEnabled(false);
                            mclient.btnDisconnect.setEnabled(true);
                            mclient.lblStatus.setText("Connected");
                            Object[] names = ClientList.clientList.toArray();
                            mclient.setNames(names);
                            mclient.namesList.setSelectedIndices(mclient.selectedClients);

                         } else if (serverMessage.startsWith("message")) {
                            String message = serverMessage;
                            
                            message = message.replace("message:", "");
                            Message parsed = parseMessage(message);
                            Messages.list.add(parsed);

                            /*UI*/
                            mclient.txtRecieved.getEditorKit().createDefaultDocument();
                            String all = "";
                            for (Message msg : Messages.list) {
                                all += msg;
                            }
                            mclient.txtRecieved.setText(all);
                        } else {
                            System.out.println(serverMessage);
                        }
                    } else {
                        break;
                    }
                }
                Thread.sleep(2000);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                try {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException io) {
                    io.printStackTrace();
                }
                break;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                break;
            }
        }
        mclient.btnLogin.setEnabled(true);
        mclient.btnSend.setEnabled(false);
        mclient.btnDisconnect.setEnabled(false);
        mclient.lblStatus.setText("Disconnected");
        Object names[] = new Object[0];
        mclient.setNames(names);
        mclient.namesList.setSelectedIndices(new int[0]);
    }
}
