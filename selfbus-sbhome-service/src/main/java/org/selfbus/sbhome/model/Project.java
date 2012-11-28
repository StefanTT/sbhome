package org.selfbus.sbhome.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.freebus.fts.common.address.GroupAddress;
import org.selfbus.sbhome.model.group.Group;
import org.selfbus.sbhome.model.gui.PanelDecl;
import org.selfbus.sbhome.model.program.Program;

/**
 * A project.
 */
@XmlType(name = "project", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Project
{
   @XmlAttribute
   private String name;

   @XmlAttribute
   private String startPanel;

   private Map<GroupAddress, Group> groups;
   private Map<String, Group> groupsByName;
   private Map<String, Category> categories;
   private Map<String, Room> rooms;
   private List<PanelDecl> panels;
   private Set<Program> programs;

   /**
    * Gets the value of the name property.
    * 
    * @return possible object is {@link String }
    * 
    */
   public String getName()
   {
      return name;
   }

   /**
    * Sets the value of the name property.
    * 
    * @param value allowed object is {@link String }
    * 
    */
   public void setName(String value)
   {
      this.name = value;
   }

   /**
    * @return The ID of the start panel.
    * 
    */
   public String getStartPanel()
   {
      return startPanel;
   }

   /**
    * Set the ID of the start panel.
    * 
    * @param id - the ID to set
    * 
    */
   public void setStartPanel(String id)
   {
      this.startPanel = id;
   }

   /**
    * Get a specific group.
    * 
    * @param addr - the address of the group to get.
    * @return The group, or null if the address is unknown.
    */
   public Group getGroup(GroupAddress addr)
   {
      return groups.get(addr);
   }

   /**
    * Get a specific group.
    * 
    * @param id - the ID of the group to get.
    * @return The group.
    * @throws IllegalArgumentException if no group with the ID exists
    */
   public Group getGroup(String id)
   {
      Validate.isTrue(groupsByName.containsKey(id), "invalid group ID \"" + id + "\"");
      return groupsByName.get(id);
   }

   /**
    * Get a category.
    * 
    * @param id - the ID of the category
    * 
    * @return The category, or null if not found.
    */
   public Category getCategory(String id)
   {
      return categories.get(id);
   }

   /**
    * @return The categories
    */
   @XmlElementWrapper(name = "categories")
   @XmlElement(name = "category")
   public List<Category> getCategories()
   {
      if (categories == null)
         return new Vector<Category>(1);

      List<Category> result = new Vector<Category>(categories.size());
      result.addAll(categories.values());

      return result;
   }

   /**
    * @param categories - the categories to set
    */
   public void setCategories(List<Category> categories)
   {
      final Map<String, Category> newCategories = new HashMap<String, Category>(categories.size() * 3);

      for (final Category category : categories)
         newCategories.put(category.getId(), category);

      this.categories = newCategories;
   }

   /**
    * @return The groups
    */
   @XmlElementWrapper(name = "groups")
   @XmlElement(name = "group")
   public List<Group> getGroups()
   {
      final List<Group> result = new Vector<Group>(groups == null ? 1 : groups.size());

      if (groups != null)
         result.addAll(groups.values());

      return result;
   }

   /**
    * @param groups - the groups to set
    */
   public void setGroups(List<Group> groups)
   {
      final Map<GroupAddress, Group> newGroups = new HashMap<GroupAddress, Group>(groups.size() * 3);
      final Map<String, Group> newGroupsByName = new HashMap<String, Group>(groups.size() * 3);

      for (final Group group : groups)
      {
         newGroups.put(group.getAddr(), group);
         newGroupsByName.put(group.getId(), group);
      }

      this.groups = newGroups;
      this.groupsByName = newGroupsByName;
   }

   /**
    * @return The programs.
    */
   @XmlElementWrapper(name = "programs")
   @XmlElement(name = "program")
   public synchronized Set<Program> getPrograms()
   {
      if (programs == null)
         programs = new HashSet<Program>();

      return programs;
   }

   /**
    * Set the programs.
    *
    * @param programs - the programs to set
    */
   public void setPrograms(Set<Program> programs)
   {
      this.programs = programs;
   }

   /**
    * Get a room.
    * 
    * @param id - the ID of the room
    * 
    * @return The room, or null if not found.
    */
   public Room getRoom(String id)
   {
      return rooms.get(id);
   }

   /**
    * @return The rooms
    */
   @XmlElementWrapper(name = "rooms")
   @XmlElement(name = "room")
   public List<Room> getRooms()
   {
      if (rooms == null)
         return new Vector<Room>(1);

      List<Room> result = new Vector<Room>(rooms.size());
      result.addAll(rooms.values());

      return result;
   }

   /**
    * @param rooms - the rooms to set
    */
   public void setRooms(List<Room> rooms)
   {
      final Map<String, Room> newRooms = new HashMap<String, Room>(rooms.size() * 3);

      for (final Room room : rooms)
         newRooms.put(room.getId(), room);

      this.rooms = newRooms;
   }

   /**
    * Get a panel by ID.
    * 
    * @param id - the ID of the panel.
    * 
    * @return The panel, or null if not found.
    */
   public PanelDecl getPanel(final String id)
   {
      for (final PanelDecl panel : panels)
      {
         if (id.equals(panel.getId()))
            return panel;
      }

      return null;
   }

   /**
    * @return the panels
    */
   @XmlElementWrapper(name = "panels")
   @XmlElement(name = "panel")
   public List<PanelDecl> getPanels()
   {
      return panels;
   }

   /**
    * @param panels the panels to set
    */
   public void setPanels(List<PanelDecl> panels)
   {
      this.panels = panels;
   }
}
