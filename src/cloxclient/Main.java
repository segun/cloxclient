/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient;

import com.trinisoft.cloxclient.ui.CloxClient;
import com.trinisoft.libraries.Centralizer;

/**
 *
 * @author segun
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CloxClient client = new CloxClient();
        Centralizer.centralize(client);
        client.setVisible(true);
    }
}
