package org.selfbus.sbhome.service.model.module;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.model.base.Namespaces;
import org.selfbus.sbhome.service.model.variable.Variable;
import org.selfbus.sbhome.service.process.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A module
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class Module extends AbstractModule
{
   static final Logger LOGGER = LoggerFactory.getLogger(Module.class);

   @XmlAttribute(name = "type", required = true)
   private String moduleTypeName;

   ModuleType moduleType;
   /**
    * @return The name of the module type.
    */
   public String getModuleTypeName()
   {
      return moduleType == null ? moduleTypeName : moduleType.getName();
   }

   /**
    * @return The module type.
    */
   public ModuleType getModuleType()
   {
      return moduleType;
   }

   /**
    * Set the module type.
    * 
    * @param moduleType - the module type
    */
   public void setModuleType(ModuleType moduleType)
   {
      this.moduleType = moduleType;
      setupVariables(moduleType.getDeclarations());
   }

   /**
    * Execute the module's script once.
    */
   @Override
   public void execute()
   {
      LOGGER.debug("Executing module {}", getName());
      String varNamePrefix = getName() + '.';

      JexlEngine jexl = Daemon.getInstance().getScriptEngine();
      synchronized (jexl)
      {
         Script script = moduleType.getScript();

         Context readCtx = new Context();
         for (Variable var : vars.values())
            readCtx.set(var.getName().substring(varNamePrefix.length()), var.getValue());

         Context writeCtx = new Context(readCtx);

         script.execute(writeCtx);

         for (String varName : writeCtx.localKeySet())
         {
            Variable var = vars.get(varNamePrefix + varName);
            if (var != null)
               var.setValue(writeCtx.get(varName));
         }
      }
   }
}
