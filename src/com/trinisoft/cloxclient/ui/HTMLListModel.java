/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.cloxclient.ui;

import com.trinisoft.cloxclient.models.Message;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author trinisoftinc
 */
public class HTMLListModel implements ListModel {

    ArrayList<Message> data;

    public HTMLListModel(ArrayList<Message> data) {
        this.data = data;
    }

    public void addListDataListener(ListDataListener l) {
    }

    public Object getElementAt(int index) {        
        String originalMessage = data.get(index).getMsg();
        String toReturn = "";        
        if (originalMessage.length() > 50) {
            originalMessage = lineBreaks(originalMessage);
        }
        System.out.println(originalMessage);
        toReturn += originalMessage;
        Message oneMessage = data.get(index);
        String myData = "<i>" + oneMessage.getTime() + "</i> <b style='color:green'>from " + oneMessage.getFrom() + "</b> " +
                "<b style='color:red'>to " + oneMessage.getTo() + "</b><br />" +
                "<div style='margin-left:25px'>" + toReturn + "</div><br />";
        System.out.println(myData);
        return "<html>" + myData + "</html>";
    }

    public int getSize() {
        return data.size();
    }

    public void removeListDataListener(ListDataListener l) {
    }

    private String lineBreaks(String msg) {
        String retVal = "";
        String prev = "";
        StringTokenizer tokenizer = new StringTokenizer(msg, " ", true);
        while(tokenizer.hasMoreTokens()) {            
            String thisToken = tokenizer.nextToken();
            if((prev + thisToken).length() > 55) {
                retVal += "<br>";
                prev = "";
            }
            retVal += thisToken;
            prev += thisToken;
        }
        return retVal;
    }
}
