/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient;

import com.trinisoft.cloxclient.Client;
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
        //Client.host = "frontdesk";
        Client.host = "unotifier.com";
        Client.port = 1981;
        Centralizer.centralize(client);
        client.setVisible(true);
    }
}
