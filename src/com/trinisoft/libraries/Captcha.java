/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.libraries;

import java.util.Random;

/**
 *
 * @author trinisoftinc
 */
public class Captcha {

    private int q1;
    private int q2;
    private String operation;
    private String question;
    private String[] operations = {
        "+", "-", "*"
    };
    //private String question;

    public Captcha() {
    }

    public void createQuestion() {
        Random r = new Random(System.currentTimeMillis());

        int operationRandInt = r.nextInt(3);
        String operationString = operations[operationRandInt];

        int q1Rand = r.nextInt(10) + 1;
        int q2Rand = r.nextInt(10) + 1;

        //we don't want answers to have -negative results
        //thanks to carlos, we must make sure that when q2Rand == 10, we don't allow the loop to hang
        if (operationString.equals("-")) {
            while (q2Rand >= q1Rand) {
                if(q2Rand == 10) {
                    q1Rand = r.nextInt(10) + 10;
                } else {
                    q2Rand = r.nextInt(10) + 1;
                }
            }
        }

        /*
        if you want to implement for division, be my guest.
        A few thoughts though.
         * 1. It will be easier if there are no reminders in answers. i.e q1/q2 = Whole Number
         * 2. It will be safer if q1 != q2.
         */

        q1 = q1Rand;
        q2 = q2Rand;
        operation = operationString;

        question = q1 + " " + operation + " " + q2;
    }

    public boolean solve(int answer) {
        if (operation.equals("+")) {
            return q1 + q2 == answer;
        } else if (operation.equals("-")) {
            return q1 - q2 == answer;
        } else if (operation.equals("*")) {
            return q1 * q2 == answer;
        }
        return false;
    }

    public String getQuestion() {
        return question;
    }
}
