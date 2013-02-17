package org.selfbus.sbhome.web;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.freebus.fts.common.Environment;
import org.selfbus.sbhome.web.servlets.FileUploadServlet;
import org.selfbus.sbhome.web.servlets.ProjectServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Acme.Serve.Serve;

import com.vaadin.terminal.gwt.server.ApplicationServlet;

/**
 * The web server.
 */
public class WebServer extends Serve
{
   private static final long serialVersionUID = 5591766791363378470L;
   private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);

   private static int httpPort = 8182;

   /**
    * Create a web server.
    */
   WebServer()
   {
      super(new Properties(), System.err);
      
      final Properties args = (Properties) arguments;
      args.put("port", httpPort);
      args.put(Serve.ARG_NOHUP, "nohup");

      PathTreeDictionary aliases = new PathTreeDictionary();
      aliases.put("/", getResourceBase());
      setMappingTable(aliases);

      addServlet("/static", new FileUploadServlet());
      addServlet("/project", new ProjectServlet());

      Properties servletProps = new Properties();
      servletProps.put("application", SbHomeApplication.class.getName());
      addServlet("/", new ApplicationServlet(), servletProps);

      addDefaultServlets(null);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public URL getResource(String path) throws MalformedURLException
   {
      try
      {
         URL resourceUrl = super.getResource(path);
         if (resourceUrl != null)
            return resourceUrl;
      }
      catch (NullPointerException e)
      {
         LOGGER.warn("File not found: " + path);
         // Ignore, as it means: file not found
      }

      return getClass().getResource(path);
   }

   /**
    * @return The resource base directory of the web application.
    */
   private static File getResourceBase()
   {
      File f = new File("src/main/webapp");
      if (f.exists())
         return f;

      return new File("webapp");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void log(String message)
   {
      LOGGER.info(message);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void log(String message, Throwable throwable)
   {
      LOGGER.info(message, throwable);
   }

   /**
    * Launch the embedded web server.
    * 
    * @param args - the command line arguments
    * @throws IOException
    */
   public static void main(String... args) throws IOException
   {
      Environment.setAppName("sbhome");
      Environment.init();

      final WebServer server = new WebServer();

      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
      {
         public void run()
         {
            try
            {
               server.notifyStop();
            }
            catch (IOException e)
            {
               LOGGER.error("exception while stopping the server", e);
            }

            server.destroyAllServlets();
         }
      }));

      server.init();
      server.serve();
   }
}
