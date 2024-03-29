/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.cloxclient.handlers;

import com.trinisoft.cloxclient.Client;
import com.trinisoft.cloxclient.helpers.ClientList;
import com.trinisoft.cloxclient.models.Message;
import com.trinisoft.cloxclient.models.Messages;
import com.trinisoft.cloxclient.ui.CloxClient;
import com.trinisoft.cloxclient.ui.HTMLListModel;
import java.awt.Component;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

/**
 *
 * @author segun
 */
public class ProtocolHandler extends Thread {

    Socket socket;
    CloxClient mclient;
    public static File ackFile;

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
                        if(!serverMessage.startsWith("clients")) {
                            Toolkit.getDefaultToolkit().beep();
                            mclient.toFront();
                        }
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
                            mclient.messageList.setCellRenderer(new ListCellRenderer() {

                                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                                    return new JLabel(value.toString());
                                }
                            });
                            mclient.messageList.setModel(new HTMLListModel(Messages.list));
                            mclient.messageList.ensureIndexIsVisible(Messages.list.size() - 1);
                            /*UI*/
//                            String all = "<html>";
//
//                            for (int i = 0; i < Messages.list.size(); i++) {
//                                if(i == Messages.list.size() - 1) {
//                                    all += Messages.list.get(i).toNamedString();
//                                } else {
//                                    all += Messages.list.get(i).toString();
//                                }
//                            }
                            /*for(int i = (Messages.list.size() - 1); i >= 0; i--) {
                            all += Messages.list.get(i).toString();
                            }*/

                        } else if (serverMessage.startsWith("ackfile")) {
                            serverMessage = serverMessage.replace("ackfile:", "");
                            String fp[] = serverMessage.split(":s");
                            int port = 0;

                            for (String each : fp) {
                                if (each.startsWith("port")) {
                                    port = Integer.parseInt(each.replace("port=", ""));
                                    break;
                                }
                            }

                            System.out.println("PORT = " + port);
                            Socket socket1 = new Socket(Client.host, port);
                            OutputStream out = socket1.getOutputStream();
                            FileInputStream fis = new FileInputStream(ackFile);
                            int ch;

                            while ((ch = fis.read()) != -1) {
                                out.write(ch);
                            }
                            out.flush();
                            out.close();
                            JOptionPane.showMessageDialog(mclient, "File Successfully Sent");
                        } else if (serverMessage.startsWith("file")) {
                            serverMessage = serverMessage.replace("file:", "");
                            String fp[] = serverMessage.split(":s");
                            int port = 0;
                            String filename = "";
                            String from = "";

                            for (String each : fp) {
                                if (each.startsWith("port")) {
                                    port = Integer.parseInt(each.replace("port=", ""));
                                }
                                if (each.startsWith("filename")) {
                                    filename = each.replace("filename=", "");
                                }
                                if (each.startsWith("from")) {
                                    from = each.replace("from=", "");
                                }
                            }

                            System.out.println("PORT = " + port + "filename: " + filename + " from = " + from);
                            int retval = JOptionPane.showConfirmDialog(mclient, from + " is trying to send you a file. "
                                    + "\n FileName: " + filename + "\n Do you want to accept?",
                                    "Clox Client: File Transfer",
                                    JOptionPane.YES_NO_CANCEL_OPTION);
                            if (retval == JOptionPane.YES_OPTION) {
                                JFileChooser fileChooser = new JFileChooser();
                                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                fileChooser.setDialogTitle("Please select directory to store file in");
                                retval = fileChooser.showOpenDialog(mclient);
                                if (retval == JFileChooser.APPROVE_OPTION) {
                                    Socket socket1 = new Socket(Client.host, port);
                                    File f = new File(fileChooser.getSelectedFile(), filename);

                                    FileOutputStream fos = new FileOutputStream(f);
                                    InputStream is = socket1.getInputStream();

                                    int ch;

                                    while ((ch = is.read()) != -1) {
                                        fos.write(ch);
                                    }
                                    fos.flush();
                                    fos.close();

                                    JOptionPane.showMessageDialog(mclient,
                                            "File " + filename + " Saved", "Clox Client : File Transfer",
                                            JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
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
