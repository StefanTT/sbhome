package org.selfbus.sbhome.model.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;
import org.apache.commons.lang3.Validate;
import org.selfbus.sbhome.model.base.AbstractNamed;
import org.selfbus.sbhome.model.base.Namespaces;
import org.selfbus.sbhome.model.variable.VariableDeclaration;
import org.selfbus.sbhome.service.Daemon;

/**
 * A module type
 */
@XmlType(namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class ModuleType extends AbstractNamed
{
   private final Map<String,VariableDeclaration> decls = new HashMap<String, VariableDeclaration>();
   private String code;
   private Script script;

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

      script = null;
      getScript();
   }

   /**
    * Get a variable declaration.
    * 
    * @param name - the name of the connector.
    * @return The variable declaration.
    */
   public VariableDeclaration getDeclaration(String name)
   {
      return decls.get(name);
   }

   /**
    * @return The variable declarations.
    */
   public Collection<VariableDeclaration> getDeclarations()
   {
      return decls.values();
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
      decls.clear();

      for (Object obj: lst)
      {
         if (obj instanceof String)
         {
            Validate.isTrue(code == null, "code block is allowed only once");
            setCode((String) obj);
         }
         else
         {
            VariableDeclaration decl = (VariableDeclaration) obj;
            decls.put(decl.getName(), decl);
         }
      }
   }

   /**
    * Get the contents of the object. This is a method that is meant for JAXB.
    * @return The object's contents.
    */
   protected List<Object> getContents()
   {
      List<Object> result = new ArrayList<Object>(decls.size() + 1);
      result.addAll(decls.values());
      result.add(code);
      return result;
   }

   /**
    * @return The script.
    */
   public synchronized Script getScript()
   {
      if (script == null)
      {
         JexlEngine jexl = Daemon.getInstance().getScriptEngine();
         synchronized (jexl)
         {
            script = jexl.createScript(code);
         }
      }

      return script;
   }
}
