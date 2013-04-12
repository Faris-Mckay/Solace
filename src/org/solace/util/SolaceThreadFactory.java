package org.solace.util;

import java.util.concurrent.ThreadFactory;

/**
 *
 * @author Faris
 */
public class SolaceThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        return thread;
    }

}
