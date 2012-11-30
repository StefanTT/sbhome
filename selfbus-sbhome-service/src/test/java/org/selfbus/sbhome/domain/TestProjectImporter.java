package org.selfbus.sbhome.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.freebus.knxcomm.application.DataPointType;
import org.junit.Test;
import org.selfbus.sbhome.model.Project;
import org.selfbus.sbhome.model.ProjectImporter;
import org.selfbus.sbhome.model.group.Group;


public class TestProjectImporter
{
   private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

   @Test
   public void readProject() throws FileNotFoundException
   {
      final String data = XML_HEADER + 
         "<project name=\"project-1\" startPanel=\"panel.1\">\n" +
         " <categories><category id=\"cat1\" /></categories>\n" +
         " <groups>\n" +
         "  <group id=\"group.1\" addr=\"1/0/100\" dataType=\"bool\" category=\"cat1\" />\n" +
         " </groups>\n" +
         " <panels>\n" +
         "  <panel id=\"panel.1\">\n" +
         "   <label id=\"label.1\" x=\"20\" y=\"10\" width=\"100\" height=\"25\" text=\"a label\" />\n" +
         "   <button id=\"button.1\" x=\"20\" y=\"50\" width=\"100\" height=\"25\" text=\"push me\" />\n" +
         "  </panel>\n" +
         " </panels>\n" +
         "</project>";
      final InputStream in = new ByteArrayInputStream(data.getBytes());

      final ProjectImporter importer = new ProjectImporter();
      final Project project = importer.readProject(in); 
      assertNotNull(project);
      assertEquals("project-1", project.getName());
      assertEquals(1, project.getGroups().size());
      assertEquals(1, project.getPanels().size());
      assertNotNull("panel's childs not loaded", project.getPanels().get(0).getChilds());
      assertEquals(2, project.getPanels().get(0).getChilds().size());
   }

   @Test
   public void loadExampleProject() throws FileNotFoundException
   {
      File file = new File(getClass().getClassLoader().getResource("TestProjectImporter.project.xml").getPath());

      final ProjectImporter importer = new ProjectImporter();
      final Project project = importer.readProject(file); 
      assertNotNull(project);

      Group group = project.getGroup("light.1");
      assertNotNull(group);
      assertEquals(DataPointType.BOOL, group.getDataType());
      assertNotNull(group.getCategory());
      assertEquals("light", group.getCategory().getId());
   }
}
