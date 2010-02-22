/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinisoft.cloxclient.ui;

import com.trinisoft.cloxclient.models.Message;
import java.util.ArrayList;
import javax.swing.JLabel;
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
        return "<html>" + data.get(index).toString() + "<html>";
    }

    public int getSize() {
        return data.size();
    }

    public void removeListDataListener(ListDataListener l) {
        
    }

}
