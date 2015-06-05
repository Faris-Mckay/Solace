/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
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