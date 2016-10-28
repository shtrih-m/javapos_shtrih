/* ================================================================
 * JBarcode : Java Barcode Library
 * ================================================================
 *
 * Project Info:  http://jbcode.sourceforge.net
 * Project Lead:  Fl�vio Sampaio (flavio@ronisons.com);
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
package org.jbarcode.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.jmge.gif.Gif89Encoder;

import com.keypoint.PngEncoder;



/**
 * This is a utility class to image encode assistence.
 * 
 * @author Flavio Sampaio
 * @since 0.1
 */
public class ImageUtil {
	
	public static final String JPEG = "jpeg";
	public static final String PNG = "png";
	public static final String GIF = "gif";
	
	public static final int DEFAULT_DPI = 96;
	
	/**
	 * Creates a image encoded on specified format. 
	 * 
	 * @param image Image to be encoded.
	 * @param format Image format (PNG or JPEG).
	 * @return Byte array with encoded image.
	 * @throws IOException
	 */
	public static byte[] encode(BufferedImage image, String format) throws IOException{
		return encode(image, format, 96, 96);
	}
	
	/**
	 * Creates a image encoded on specified format an dpi. 
	 * 
	 * @param image Image to be encoded.
	 * @param format Image format (PNG or JPEG).
	 * @param xDpi Horizontal dpi definition.
	 * @param yDpi Vertical dpi definition.
	 * @return Byte array with encoded image.
	 * @throws IOException
	 */
	public static byte[] encode(BufferedImage image, String format, int xDpi, int yDpi) throws IOException{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			encodeAndWrite(image, format, out, xDpi, yDpi);
			return out.toByteArray();
	}
	
	public static void encodeAndWrite(BufferedImage image, String format, OutputStream out) throws IOException{
		encodeAndWrite(image, format, out, DEFAULT_DPI, DEFAULT_DPI);
	}
	
	public static void encodeAndWrite(BufferedImage image, String format, OutputStream out, int xDpi, int yDpi) throws IOException{
		if(PNG.equals(format)){
			PngEncoder pngEncoder = new PngEncoder(image);
			pngEncoder.setDpi(xDpi, yDpi);
			out.write(pngEncoder.pngEncode());
		} else if(JPEG.equals(format)){
			ImageIO.write(image, format, out);
		} else if(GIF.equals(format)){
			Gif89Encoder gifenc = new Gif89Encoder();
			gifenc.addFrame(image);
			gifenc.setComments("JBarcode (http://jbarcode.ronisons.com)");
			gifenc.encode(out);
		}else {
			throw new IOException("Unsupported image type");
		}
	}

}
