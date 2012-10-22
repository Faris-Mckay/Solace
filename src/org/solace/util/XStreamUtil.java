package org.solace.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author Faris
 */
public class XStreamUtil {
    
        private static XStreamUtil instance = new XStreamUtil();
	private static XStream xStream = new XStream();
        
        public static XStream getXStream() {
		return xStream;
	}
	
        /**
         * Singleton getter
         * @return 
         */
	public static XStreamUtil getInstance() {
		return instance;
	}
        //pre-defined attributes
        static {
                xStream.alias("npcDefinition", org.solace.game.entity.mobile.npc.NPCDefinition.class);
	}
        /**
         * Writes to the targeted XML files
         * @param object
         * @param file
         * @throws IOException 
         */
        public static void writeXML(Object object, File file) throws IOException {
            FileOutputStream out = new FileOutputStream(file);
            try {
                xStream.toXML(object, out);
                out.flush();
            } finally {
                out.close();
            }
        }

}
