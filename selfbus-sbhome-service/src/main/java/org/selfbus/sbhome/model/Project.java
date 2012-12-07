package org.selfbus.sbhome.model;

import java.util.ArrayList;
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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.freebus.fts.common.address.GroupAddress;
import org.selfbus.sbhome.model.base.Namespaces;
import org.selfbus.sbhome.model.gui.PanelDecl;
import org.selfbus.sbhome.model.module.Module;
import org.selfbus.sbhome.model.module.ModuleType;
import org.selfbus.sbhome.model.trigger.AbstractTriggerDecl;
import org.selfbus.sbhome.model.variable.Connection;
import org.selfbus.sbhome.model.variable.GroupVariable;
import org.selfbus.sbhome.model.variable.Variable;

/**
 * A project.
 * 
 * @see postLoad
 */
@XmlType(name = "project", namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Project
{
   @XmlAttribute
   private String name;

   @XmlAttribute
   private String startPanel;

   private Map<GroupAddress, GroupVariable> groupVars;
   private Map<String, Variable> vars;
   private Map<String, ModuleType> moduleTypes;
   private Map<String, Category> categories;
   private final Map<String, Room> rooms = new HashMap<String, Room>();
   private List<PanelDecl> panels;
   private final Map<String, Module> modules = new HashMap<String, Module>();
   private Set<Connection> connections;
   private Set<AbstractTriggerDecl> triggers;

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
    * Get a specific group variable by group address.
    * 
    * @param addr - the address of the variable to get.
    * @return The variable.
    * 
    * @throws IllegalArgumentException if the address is unknown.
    */
   public GroupVariable getVariable(GroupAddress addr)
   {
      Validate.isTrue(groupVars.containsKey(addr), "Group address does not exist: " + addr);
      return groupVars.get(addr);
   }

   /**
    * Get a specific variable. Group and module variables can also be accessed with this method.
    * 
    * @param name - the name of the variable to get.
    * @return The group.
    * 
    * @throws IllegalArgumentException if no group with the ID exists
    */
   public Variable getVariable(String name)
   {
      if (vars.containsKey(name))
         return vars.get(name);

      int idx = name.lastIndexOf('.');
      if (idx > 0)
      {
         String moduleName = name.substring(0, idx);
         String varName = name.substring(idx + 1);

         Module module = getModule(moduleName);
         if (module != null && module.containsVariable(name))
            return module.getVariable(name);
      }

      throw new IllegalArgumentException("Variable does not exist: " + name);
   }

   /**
    * @return The variables
    */
   @XmlElementWrapper(name = "variables")
   @XmlElement(name = "variable")
   public List<Variable> getVariables()
   {
      if (vars == null)
         return new Vector<Variable>();

      final List<Variable> result = new Vector<Variable>(vars.size());
      result.addAll(vars.values());
      return result;
   }

   /**
    * @param vars - the variables to set
    */
   public synchronized void setVariables(List<Variable> vars)
   {
      if (this.vars == null)
         this.vars = new HashMap<String, Variable>();

      for (final Variable var : vars)
         this.vars.put(var.getName(), var);
   }

   /**
    * @return The group variables
    */
   @XmlElementWrapper(name = "groups")
   @XmlElement(name = "group")
   public List<GroupVariable> getGroupVariables()
   {
      if (groupVars == null)
         return new Vector<GroupVariable>();

      final List<GroupVariable> result = new Vector<GroupVariable>(groupVars.size());
      result.addAll(groupVars.values());
      return result;
   }

   /**
    * @param groups - the groups to set
    */
   public synchronized void setGroupVariables(List<GroupVariable> groupVars)
   {
      if (this.vars == null)
         this.vars = new HashMap<String, Variable>();

      this.groupVars = new HashMap<GroupAddress, GroupVariable>();

      for (GroupVariable var : groupVars)
      {
         this.vars.put(var.getName(), var);
         this.groupVars.put(var.getAddr(), var);
      }
   }

   /**
    * @return The module types.
    */
   @XmlElementWrapper(name = "moduleTypes")
   @XmlElement(name = "moduleType")
   public List<ModuleType> getModuleTypes()
   {
      final List<ModuleType> result = new Vector<ModuleType>(moduleTypes == null ? 1 : moduleTypes.size());

      if (moduleTypes != null)
         result.addAll(moduleTypes.values());

      return result;
   }

   /**
    * @param moduleTypes - the module types to set
    */
   public void setModuleTypes(List<ModuleType> moduleTypes)
   {
      Map<String, ModuleType> newModuleTypes = new HashMap<String, ModuleType>(moduleTypes.size() * 3);

      for (ModuleType moduleType : moduleTypes)
      {
         if (newModuleTypes.containsKey(moduleType.getName()))
            throw new IllegalArgumentException("module type name \"" + moduleType.getName() + "\" is not unique");

         newModuleTypes.put(moduleType.getName(), moduleType);
      }

      this.moduleTypes = newModuleTypes;
   }

   /**
    * Get a module.
    * 
    * @param name - the name of the module to get.
    * @return The group.
    * 
    * @throws IllegalArgumentException if no module with the name exists
    */
   public Module getModule(String name)
   {
      Validate.isTrue(modules.containsKey(name), "Module does not exist: " + name);
      return modules.get(name);
   }

   /**
    * @return The modules.
    */
   @XmlElementWrapper(name = "modules")
   @XmlElement(name = "module")
   public List<Module> getModules()
   {
      if (modules == null)
         return new ArrayList<Module>();

      final List<Module> result = new Vector<Module>(modules.size());
      result.addAll(modules.values());
      return result;
   }

   /**
    * Set the modules.
    * 
    * @param modules - the modules to set
    */
   public void setModules(List<Module> modules)
   {
      this.modules.clear();

      for (Module module : modules)
         this.modules.put(module.getName(), module);
   }

   /**
    * @return The connections.
    */
   @XmlElementWrapper(name = "connections")
   @XmlElement(name = "connection")
   public synchronized Set<Connection> getConnections()
   {
      if (connections == null)
         connections = new HashSet<Connection>();

      return connections;
   }

   /**
    * Set the connections.
    * 
    * @param connections - the connections to set
    */
   public synchronized void setConnections(Set<Connection> connections)
   {
      this.connections = connections;
   }

   /**
    * Get a room.
    * 
    * @param name - the name of the room
    * 
    * @return The room, or null if not found.
    */
   public Room getRoom(String name)
   {
      return rooms.get(name);
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
      this.rooms.clear();

      for (final Room room : rooms)
      {
         if (this.rooms.containsKey(room.getId()))
            throw new IllegalArgumentException("Room name is not unique: " + room.getId());

         this.rooms.put(room.getId(), room);
      }
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

   /**
    * @return The global triggers
    */
   @XmlElementWrapper(name = "triggers")
   @XmlElements
   ({
      @XmlElement(name = "cronTrigger")
   })
   public Set<AbstractTriggerDecl> getTriggers()
   {
      return triggers;
   }

   /**
    * Set the global triggers.
    *
    * @param triggers - the triggers to set
    */
   public void setTriggers(Set<AbstractTriggerDecl> triggers)
   {
      this.triggers = triggers;
   }

   /**
    * To be called after loading a project.
    * 
    * The ProjectImporter's readProject methods call this method.
    */
   public void postLoad()
   {
      setupModules();
      setupConnections();
   }

   /**
    * Setup the modules.
    */
   protected void setupModules()
   {
      for (Module module : modules.values())
      {
         ModuleType moduleType = moduleTypes.get(module.getModuleTypeName());
         if (moduleType == null)
         {
            throw new IllegalArgumentException("Module \"" + module.getName() + "\" has an unknown module type \""
               + module.getModuleTypeName() + "\"");
         }

         module.setModuleType(moduleType);
      }
   }

   /**
    * Setup the connections.
    */
   protected void setupConnections()
   {
      if (connections == null)
         return;

      for (Connection connection : connections)
      {
         Variable varFrom = getVariable(connection.getFrom());
         Variable varTo = getVariable(connection.getTo());

         if (varFrom.getType() != varTo.getType())
         {
            throw new IllegalArgumentException("Cannot connect from " + connection.getFrom() + " to "
               + connection.getTo() + ": type mismatch");
         }

         varFrom.addListener(varTo);
      }
   }
}
