package org.selfbus.sbhome.service.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang3.Validate;
import org.xml.sax.SAXException;

/**
 * An importer that creates a project from an XML project export file or stream.
 */
@XmlTransient
public class ProjectImporter
{
   /**
    * Create a project importer.
    */
   public ProjectImporter()
   {
   }

   /**
    * Read the file and create the project it contains.
    * 
    * @param file - the file to read.
    * 
    * @return The created project.
    * @throws FileNotFoundException if the file does not exist
    */
   public Project readProject(File file) throws FileNotFoundException
   {
      return readProject(new FileInputStream(file));
   }

   /**
    * Read the input stream and create the project it contains.
    * 
    * @param stream - the stream to read.
    * 
    * @return The created project.
    */
   public Project readProject(InputStream stream)
   {
      Validate.notNull(stream, "input stream is null");

      try
      {
         final JAXBContext context = JAXBContext.newInstance("org.selfbus.sbhome.service.model");

         final String schemaFileName = "schema1.xsd";
         final URL schemaUrl = getClass().getClassLoader().getResource(schemaFileName);
         if (schemaUrl == null)
            throw new RuntimeException("Schema file not found in class path: " + schemaFileName);

         Unmarshaller unmarshaller = context.createUnmarshaller();
         unmarshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl));

         @SuppressWarnings("unchecked")
         JAXBElement<Project> root = (JAXBElement<Project>) unmarshaller.unmarshal(stream);

         Project project = root.getValue();
         project.postLoad();

         return project;
      }
      catch (JAXBException e)
      {
         throw new RuntimeException(e);
      }
      catch (SAXException e)
      {
         throw new RuntimeException(e);
      }
   }
}
