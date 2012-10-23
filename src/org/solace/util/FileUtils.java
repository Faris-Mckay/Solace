package org.solace.util;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * File managing utilities.
 * 
 * @author kLeptO <http://www.rune-server.org/members/klepto/>
 */
public class FileUtils {

	/**
	 * Reads string from a given data input stream.
	 * 
	 * @param in
	 *            the input stream
	 * 
	 * @return the builded string
	 */
	public static String readString(DataInputStream in) {
		byte data;
		StringBuilder builder = new StringBuilder();
		try {
			while ((data = in.readByte()) != 0) {
				builder.append((char) data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

}