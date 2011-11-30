package com.trinisoft.libraries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author root
 */
public class TemplateHelper {

    public static StringBuffer InsertToTemplate(HashMap<String, String> replacements, File templateFile) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile)));
        StringBuffer output = new StringBuffer();
        String line = "";
        Set<String> templateSymbols = replacements.keySet();
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            for (String symbols : templateSymbols) {
                System.out.println(symbols);
                if (line.contains("${")) {
                    if (line.contains(symbols)) {
                        line = line.replace("${" + symbols + "}", replacements.get(symbols));
                    }
                }
            }
            output.append(line);
            output.append("\n");
        }
        return output;
    }

    public static void main(String args[]) {
        HashMap<String, String> replacements = new HashMap<String, String>();
        replacements.put("name", "Segun");
        replacements.put("age", "33");
        replacements.put("gender", "male");
        replacements.put("email", "segun@gmail.com");
        replacements.put("best food", "rice and beans");
        replacements.put("company", "blumobiletech");

        File fromFile = new File("/home/segun/test.tpl");
        try {
            StringBuffer after = TemplateHelper.InsertToTemplate(replacements, fromFile);
            System.out.println(after);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
