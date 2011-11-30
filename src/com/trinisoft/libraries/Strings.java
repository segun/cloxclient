/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinisoft.libraries;

/**
 *
 * @author trinisoftinc
 */
public class Strings {
    public static String concat(String ... strings) {
        String retval = "";
        for(String s: strings) {
            retval += s;
        }
        return retval;
    }
}
