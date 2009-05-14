/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.cloxclient.handlers;

import com.trinisoft.cloxclient.helpers.ClientList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author segun
 */
public class ClientsReader extends Thread {

    Socket socket;

    public ClientsReader(Socket socket) {
        this.socket = socket;
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
                String clients = reader.readLine();
                if (clients != null) {
                    if (clients.startsWith("clients")) {
                        clients = clients.replace("clients:", "");
                        updateClientList(clients);
                    }
                } else {
                    break;
                }
                Thread.sleep(2000);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                break;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                break;
            }
        }
    }
}
