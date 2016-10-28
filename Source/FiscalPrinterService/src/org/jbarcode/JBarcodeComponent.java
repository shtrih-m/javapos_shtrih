/* ================================================================
 * JBarcode : Java Barcode Library
 * ================================================================
 *
 * Project Info:  http://jbcode.sourceforge.net
 * Project Lead:  Flávio Sampaio (flavio@ronisons.com);
 *
 * (C) Copyright 2005, by Favio Sampaio
 *
 * This library is free software; you can redistribute it and/or modify it underthe terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jbarcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.jbarcode.encode.BarcodeEncoder;
import org.jbarcode.encode.InvalidAtributeException;
import org.jbarcode.paint.BarcodePainter;
import org.jbarcode.paint.TextPainter;





/**
 * TODO: Description.
 * 
 * @author Flávio Sampaio
 * @since 0.1
 */
public class JBarcodeComponent extends JComponent implements PropertyChangeListener{

    private static final long serialVersionUID = 1L;
    
    private JBarcode jbarcode;
    private BufferedImage img;
    private String text;
    private String checkSum;
    
    public JBarcodeComponent(JBarcode jbarcode){
        this(jbarcode, "");
    }
    
    public JBarcodeComponent(JBarcode jbarcode, String text){
        this.jbarcode = jbarcode;
        addPropertyChangeListener(this);
        if(text != null){
            try {
                setText(text);
            } catch (InvalidAtributeException exc) {}
        }
        this.setBackground(Color.white);
    }
    
    public void paint(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        if(img != null){
            int x = (getWidth() - img.getWidth())/2;
            int y = (getHeight() -img.getHeight())/2;            
            g.drawImage(img, x, y, this);
        }
        super.paint(g);
    }



    /**
     * @return Returns the text.
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The text to set.
     * @throws InvalidAtributeException 
     */
    public void setText(String text) throws InvalidAtributeException {
        if(text.equals(getText())){
            return;
        }
        String old = this.text;
        this.text = text;
        this.checkSum = jbarcode.calcCheckSum(text);
        firePropertyChange("Text", old, text);
        invalidate();
        repaint();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#getBarHeigth()
     */
    public double getBarHeight() {
        return jbarcode.getBarHeight();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#getEncoder()
     */
    public BarcodeEncoder getEncoder() {
        return jbarcode.getEncoder();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#getNarrowWidth()
     */
    public double getXDimension() {
        return jbarcode.getXDimension();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#getPainter()
     */
    public BarcodePainter getPainter() {
        return jbarcode.getPainter();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#getWideToNarrow()
     */
    public double getWideRatio() {
        return jbarcode.getWideRatio();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setBarHeigth(int)
     */
    public void setBarHeight(double barHeight) {
        if(getBarHeight() == barHeight){
            return;
        }
        double old = getBarHeight();
        jbarcode.setBarHeight(barHeight);
        firePropertyChange("BarHeigth", old, barHeight);
        invalidate();
        repaint();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setEncoder(org.jbarcode.encode.BarcodeEncoder)
     */
    public void setEncoder(BarcodeEncoder bcencoder) {
        if(getEncoder().equals(bcencoder)){
            return;
        }
        Object old = getEncoder();
        jbarcode.setEncoder(bcencoder);
        firePropertyChange("Encoder", old, bcencoder);
        invalidate();
        repaint();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setNarrowWidth(int)
     */
    public void setXDimension(double xdim) throws InvalidAtributeException {
        if(getXDimension() == xdim){
            return;
        }
        double old = getXDimension();
        jbarcode.setXDimension(xdim);
        firePropertyChange("xDimension", old, xdim);
        invalidate();
        repaint();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setPainter(org.jbarcode.paint.BarcodePainter)
     */
    public void setPainter(BarcodePainter bcpainter) {
        if(getPainter().equals(bcpainter)){
            return;
        }
        Object old = getPainter();
        jbarcode.setPainter(bcpainter);
        firePropertyChange("Painter", old, bcpainter);
        invalidate();
        repaint();
    }



    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setWideToNarrow(int)
     */
    public void setWideRatio(double wideRatio) throws InvalidAtributeException {
        if(getWideRatio() == wideRatio){
            return;
        }
        double old = getWideRatio();
        jbarcode.setWideRatio(wideRatio);
        firePropertyChange("wideRatio", old, wideRatio);
        invalidate();
        repaint();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        try {
            if(text != null && !"".equals(text) && jbarcode != null){
                img = jbarcode.createBarcode(text);
            }
        } catch (InvalidAtributeException exc) {
        }
    }

    /**
     * @return Returns the checkSum.
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     * @param checkSum The checkSum to set.
     */
    public void setCheckSum(String checkSum) {
        firePropertyChange("checkSum", this.checkSum, this.checkSum);
        invalidate();
        repaint();
    }

    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#getTextPainter()
     */
    public TextPainter getTextPainter() {
        return jbarcode.getTextPainter();
    }

    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#isCheckDigit()
     */
    public boolean isCheckDigit() {
        return jbarcode.isCheckDigit();
    }

    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#isShowCheckDigit()
     */
    public boolean isShowCheckDigit() {
        return jbarcode.isShowCheckDigit();
    }

    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#isShowText()
     */
    public boolean isShowText() {
        return jbarcode.isShowText();
    }

    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setCheckDigit(boolean)
     */
    public void setCheckDigit(boolean checkDigit) {
        if(checkDigit == isCheckDigit()){
            return;
        }
        boolean old = isCheckDigit();
        jbarcode.setCheckDigit(checkDigit);
        firePropertyChange("checkDigit", old, checkDigit);
        invalidate();
        repaint();
    }

    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setShowCheckDigit(boolean)
     */
    public void setShowCheckDigit(boolean showCheckDigit) {
        if(showCheckDigit == isShowCheckDigit()){
            return;
        }
        boolean old = isShowCheckDigit();
        jbarcode.setShowCheckDigit(showCheckDigit);
        firePropertyChange("showCheckDigit", old, showCheckDigit);
        invalidate();
        repaint();
    }

    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setShowText(boolean)
     */
    public void setShowText(boolean showText) {
        if(showText == isShowText()){
            return;
        }
        boolean old = isShowText();
        jbarcode.setShowText(showText);
        firePropertyChange("showText", old, showText);
        invalidate();
        repaint();
    }

    /* (non-Javadoc)
     * @see org.jbarcode.JBarcode#setTextPainter(org.jbarcode.paint.TextPainter)
     */
    public void setTextPainter(TextPainter textpainter) {
        if(getTextPainter().equals(textpainter)){
            return;
        }
        Object old = getTextPainter();
        jbarcode.setTextPainter(textpainter);
        firePropertyChange("textPainter", old, textpainter);
        invalidate();
        repaint();
        jbarcode.setTextPainter(textpainter);
    }
    
}