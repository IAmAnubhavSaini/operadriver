package com.opera.core.systems.scope.stp;

import java.util.logging.Logger;

import com.opera.core.systems.util.SocketMonitor;
import com.opera.core.systems.scope.handlers.IConnectionHandler;
import com.opera.core.systems.scope.handlers.AbstractEventHandler;


/**
 * This thread starts the StpConnectionListener which listens for incoming
 * connections and accepts those connections.
 *
 * This thread also owns the SocketMonitor instance - so all network traffic and
 * communication is happening on this thread.
 *
 * @author Jan Vidar Krey
 */
public class StpThread extends Thread {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private StpConnectionListener listener;
    private StpConnection stpConnection;
    private boolean running = true;
    
    public StpThread(int port, IConnectionHandler handler, AbstractEventHandler eventHandler) throws java.io.IOException
    {
        super("StpThread");
        SocketMonitor.instance();
        listener = new StpConnectionListener(port, handler, eventHandler);
        start();
    }
    
    public void shutdown() {
        running = false;
        listener.stop();
        SocketMonitor.instance().stop();
    }
    
    
    @Override
    public void run() {
        logger.info("Started StpThread.");
        while (running)
        {
            SocketMonitor.poll(60000);
        }
        logger.info("Stopping StpThread.");
    }

}