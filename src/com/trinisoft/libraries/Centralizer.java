package com.trinisoft.libraries;

import java.awt.*;


public class Centralizer {
    public static void centralize(Component c) {
        long cWidth = c.getSize().width;
        long cHeight = c.getSize().height;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        long sHeight = (long)screenSize.getHeight();
        long sWidth = (long)screenSize.getWidth();
        
        if(sHeight < cHeight) cHeight = sHeight;
        if(sWidth < cWidth) cWidth = sWidth;

        Point p = new Point((int)(sWidth - cWidth)/2, (int)(sHeight - cHeight)/2);
        c.setLocation(p);
    }
}
