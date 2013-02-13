package org.selfbus.sbhome.web;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.freebus.fts.common.Environment;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Launch the embedded web server.
 */
public class Launcher
{
   private static String contextPath = "/";
   private static int httpPort = 8182;

   /**
    * @return The resource base directory of the web application.
    */
   private static String getResourceBase()
   {
      File f = new File("src/main/webapp");
      if (f.exists())
         return "src/main/webapp";

      return "webapp";
   }

   /**
    * Launch the embedded web server.
    *
    * @param args - the command line arguments
    */
   public static void main(String[] args) throws Exception
   {
      SLF4JBridgeHandler.install();
      Environment.init();

      Server server = new Server(httpPort);

      WebAppContext webapp = new WebAppContext();
      webapp.setContextPath(contextPath);
      webapp.setResourceBase(getResourceBase());
      webapp.setClassLoader(Thread.currentThread().getContextClassLoader());

      server.setHandler(webapp);
      server.start();
      System.out.println("Started Jetty " + Server.getVersion() + ", go to http://localhost:" + httpPort + contextPath);

      server.join();
   }
}
