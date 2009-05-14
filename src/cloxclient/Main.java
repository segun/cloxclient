/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloxclient;

import com.trinisoft.cloxclient.Client;
import java.util.Random;

/**
 *
 * @author segun
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int randy = Math.abs(new Random(System.currentTimeMillis()).nextInt());
        new Client("segun_" + randy).start();
    }
}
