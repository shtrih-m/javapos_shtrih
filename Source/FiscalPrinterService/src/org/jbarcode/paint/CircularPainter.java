package org.jbarcode.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jbarcode.encode.BarSet;



public class CircularPainter implements BarcodePainter {	    
    private static BarcodePainter instance; 
    
    private CircularPainter (){
        
    }
        
    public static BarcodePainter getInstance(){
        if(instance == null){
            instance = new CircularPainter ();
        }
        return instance;
    }
    
    /* (non-Javadoc)
     * @see org.jbarcode.BarcodePainter#paint(br.ronison.util.BitSet[])
     */
    public BufferedImage paint(BarSet[] barcode, int barWidth, int barHeight, double wideRatio) {
        float width = 0;
        float wideWidth = (float)(barWidth*wideRatio);
        for (int i = 0; i < barcode.length; i++) {
            for(int j = 0; j < barcode[i].length(); j++){
                if(barcode[i].get(j)){
                    width += wideWidth;
                } else {
                    width += barWidth;
                }
            }
        }
        float dim = Math.max(2*width, 2*barHeight);
        
        BufferedImage result = new BufferedImage((int)dim, (int)dim, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = result.createGraphics();
        
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);
                
        float x = 0;
        boolean flag = true;
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, Math.round(dim), Math.round(dim));
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform at = new AffineTransform();
        at.setToTranslation(dim/2, dim/2);
        g2d.transform(at);
        x=-dim/2;
        
        for (int i = 0; i < barcode.length; i++) {
            for (int j = 0; j < barcode[i].length(); j++) {
                width = (barcode[i].get(j) ? wideWidth : barWidth);
                
                //Change color
                if(flag){
                	int pos = (int)(x +width/2);
                	
                    g2d.setStroke(new BasicStroke(width));
                    g2d.drawOval(pos,pos,Math.abs(2*pos),Math.abs(2*pos));

                    g2d.setColor(Color.WHITE);
                } else {
                	g2d.setColor(Color.BLACK);
                }
                flag = !flag;
                x += width;
            }
        }
        return result;
    }

}

