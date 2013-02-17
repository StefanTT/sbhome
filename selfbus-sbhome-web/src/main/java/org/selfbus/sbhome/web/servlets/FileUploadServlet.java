package org.selfbus.sbhome.web.servlets;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Acme.Serve.FileServlet;

public class FileUploadServlet extends FileServlet
{
   private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadServlet.class);
   private static final long serialVersionUID = 7152927743631455007L;

   @Override
   public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
      String method = req.getMethod();

      if (method.equals("POST"))
         doPost(req, res);
      else if (method.equals("PUT"))
         doPost(req, res);
      else super.service(req, res);
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
      FileOutputStream out = new FileOutputStream("/tmp/upload.data");
      Acme.Utils.copyStream(req.getInputStream(), out, -1);
      out.close();

      res.setStatus(HttpServletResponse.SC_OK);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void log(String msg)
   {
      LOGGER.debug(msg);
   }
}
