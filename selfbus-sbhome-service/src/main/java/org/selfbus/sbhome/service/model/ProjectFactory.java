package org.selfbus.sbhome.service.model;

import java.io.File;

import org.freebus.fts.common.Environment;

/**
 * Factory class for projects.
 * 
 * TODO this class is work in progress
 */
public class ProjectFactory
{
   /**
    * Load the project.
    *
    * @return The project
    */
   public Project getProject()
   {
      return null;
   }

   /**
    * @return The file for the project. This is the location where the project is stored.
    */
   public File getProjectFile()
   {
      return new File(Environment.getAppDir() + "/project.xml");
   }
}
