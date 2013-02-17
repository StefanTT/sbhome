package org.selfbus.sbhome.web.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.selfbus.sbhome.service.Daemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet for uploading and downloading the current project.
 * 
 * Testing:
 * <pre>
 * curl --noproxy '*' -v -u test:secret1 http://localhost:8182/project
 * curl --noproxy '*' -v -u test:secret1 http://localhost:8182/project --upload-file myproject.xml
 * </pre>
 */
public class ProjectServlet extends HttpServlet
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServlet.class);
   private static final long serialVersionUID = 4244824054798898997L;

   // Test settings: user "test" password "secret1"
   private static String auth = "Basic dGVzdDpzZWNyZXQx";

   /**
    * Authenticate the access.
    *
    * @throws IOException 
    */
   protected boolean authenticate(HttpServletRequest req, HttpServletResponse res) throws IOException
   {
      String paramAuth = req.getHeader("authorization");
      if (auth.equals(paramAuth))
         return true;

      res.setStatus(HttpServletResponse.SC_FORBIDDEN);
      res.getOutputStream().println("Access denied");
      LOGGER.warn("Access denied for " + req.getRequestURI());

      return false;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
      if (!authenticate(req, res))
         return;

      OutputStream out = res.getOutputStream();
      res.setStatus(HttpServletResponse.SC_OK);

      File projectFile = Daemon.getInstance().getProjectFile();
      if (projectFile == null)
      {
         res.setStatus(HttpServletResponse.SC_NOT_FOUND);
         return;
      }

      FileInputStream in = new FileInputStream(projectFile);
      Acme.Utils.copyStream(in, out, -1);
      in.close();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
      doPut(req, res);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
      if (!authenticate(req, res))
         return;

      FileOutputStream out = new FileOutputStream("/tmp/upload.data");
      Acme.Utils.copyStream(req.getInputStream(), out, -1);
      out.close();

      res.setStatus(HttpServletResponse.SC_OK);
   }
}
