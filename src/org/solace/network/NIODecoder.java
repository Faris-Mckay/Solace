package org.solace.network;

import java.io.IOException;

/**
 *
 * @author Faris
 */
public interface NIODecoder {
    
    /**
	 * Decodes the incoming data.
	 * 
	 * @param channelContext
	 *            the associated channel context
	 */
	public void decode(RSChannelContext channelContext) throws IOException;

}
