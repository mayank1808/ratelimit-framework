/**
 * 
 */
package com.phonepe.ratelimit.datastore.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import com.phonepe.ratelimit.datastore.handler.DatastoreHandler;

/**
 * @author mayank
 *
 */
public class DatastoreServer {
	private static int port;

	public static void main(String[] args) throws Exception {
		if (port == 0) {
			Server server = new Server();
			ServerConnector conn = new ServerConnector(server);
			conn.setPort(3535);
			port = 3535;
			server.addConnector(conn);
			server.setHandler(new DatastoreHandler());

			server.setStopAtShutdown(true);
			server.start();
		}
	}
}
