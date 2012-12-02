package org.selfbus.sbhome.model.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbhome.base.AbstractNamed;
import org.selfbus.sbhome.base.Namespaces;
import org.selfbus.sbhome.model.variable.VariableDeclaration;

/**
 * A module type
 */
@XmlType(namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class ModuleType extends AbstractNamed
{
   private final Map<String, ModuleTypeConnector> connectors = new HashMap<String, ModuleTypeConnector>();
   private final Set<VariableDeclaration> varDecls = new HashSet<VariableDeclaration>();
   private String code;

   /**
    * @return The regular expression for testing names.
    */
   @Override
   protected String getNameRegex()
   {
      return "^[a-zA-Z_][\\w\\.]*$";
   }

   /**
    * @return The script code
    */
   public String getCode()
   {
      return code;
   }

   /**
    * Set the script code.
    *
    * @param code - the code to set
    */
   public void setCode(String code)
   {
      this.code = code;
   }

   /**
    * Get connector.
    * 
    * @param name - the name of the connector.
    * @return The connector, or null if not found.
    */
   public ModuleTypeConnector getConnector(String name)
   {
      return connectors.get(name);
   }

   /**
    * @return The list of connectors.
    */
   public Collection<ModuleTypeConnector> getConnectors()
   {
      return connectors.values();
   }

   /**
    * Set the contents of the object. This is a method that is meant for JAXB.
    *
    * @param lst - the contents
    */
   @XmlElements
   ({
      @XmlElement(name = "input", type = ModuleTypeInputConnector.class),
      @XmlElement(name = "output", type = ModuleTypeOutputConnector.class),
      @XmlElement(name = "variable", type = VariableDeclaration.class),
      @XmlElement(name = "code", type = String.class)
   })
   protected void setContents(List<Object> lst)
   {
      connectors.clear();
      varDecls.clear();

      for (Object obj: lst)
      {
         if (obj instanceof String)
         {
            Validate.isTrue(code == null, "code block is allowed only once");
            code = (String) obj;
         }
         else if (obj instanceof VariableDeclaration)
         {
            varDecls.add((VariableDeclaration) obj);
         }
         else
         {
            ModuleTypeConnector con = (ModuleTypeConnector) obj;
            Validate.isTrue(!connectors.containsKey(con.getName()), "connector name \"" + con.getName() + "\" used twice in module type " + name);

            connectors.put(con.getName(), con);
         }
      }
   }

   /**
    * Get the contents of the object. This is a method that is meant for JAXB.
    * @return The object's contents.
    */
   protected List<Object> getContents()
   {
      List<Object> result = new ArrayList<Object>(connectors.size() + 1);
      result.addAll(connectors.values());
      result.addAll(varDecls);
      result.add(code);
      return result;
   }
}
