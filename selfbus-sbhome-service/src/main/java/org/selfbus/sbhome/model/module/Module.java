package org.selfbus.sbhome.model.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;
import org.apache.commons.lang3.Validate;
import org.selfbus.sbhome.model.base.AbstractNamed;
import org.selfbus.sbhome.model.base.Namespaces;
import org.selfbus.sbhome.model.variable.Variable;
import org.selfbus.sbhome.model.variable.VariableDeclaration;
import org.selfbus.sbhome.model.variable.VariableListener;
import org.selfbus.sbhome.process.Context;
import org.selfbus.sbhome.service.Daemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A module
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class Module extends AbstractNamed
{
   private static final Logger LOGGER = LoggerFactory.getLogger(Module.class);

   @XmlAttribute(name = "type", required = true)
   private String moduleTypeName;

   private ModuleType moduleType;
   private final Map<String, Variable> vars = new HashMap<String, Variable>();
   private boolean scheduled;

   /**
    * Get a specific variable. Group variables can also be accessed with this method.
    * 
    * @param name - the name of the variable to get.
    * @return The group.
    * 
    * @throws IllegalArgumentException if no group with the ID exists
    */
   public Variable getVariable(String name)
   {
      Validate.isTrue(vars.containsKey(name), "Variable does not exist: " + name);
      return vars.get(name);
   }

   /**
    * Test if a variable exist.
    * 
    * @param name - the name of the variable.
    * @return True if the variable exists.
    */
   public boolean containsVariable(String name)
   {
      return vars.containsKey(name);
   }

   /**
    * @return All variables.
    */
   public Collection<Variable> getVariables()
   {
      return vars.values();
   }

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
      init();
   }

   /**
    * Execute the module's script once.
    */
   public void execute()
   {
      LOGGER.debug("Executing module {}", getName());
      
      JexlEngine jexl = Daemon.getInstance().getScriptEngine();
      synchronized (jexl)
      {
         Script script = moduleType.getScript();

         Context readCtx = new Context();
         for (Variable var : vars.values())
         {
            if (var.getValue() == null)
               var.initValue();

            readCtx.set(var.getName(), var.getValue());
         }

         Context writeCtx = new Context(readCtx);

         script.execute(writeCtx);

         for (String varName : writeCtx.localKeySet())
         {
            Variable var = vars.get(varName);
            if (var != null)
               var.setValue(writeCtx.get(varName));
         }
      }
   }

   /**
    * @return The regular expression for testing names.
    */
   @Override
   protected String getNameRegex()
   {
      return "^[a-zA-Z_][\\w\\.]*$";
   }

   /**
    * Initialize the module.
    */
   protected void init()
   {
      Validate.notNull(moduleType);

      vars.clear();
      for (VariableDeclaration decl : moduleType.getDeclarations())
      {
         Variable var = new Variable(decl);
         vars.put(var.getName(), var);

         if (decl instanceof ModuleTypeInputConnector)
            var.addListener(inputVariableChangedListener);
      }
   }

   /**
    * @return True if the module is scheduled for execution.
    */
   public boolean isScheduled()
   {
      return scheduled;
   }

   /**
    * Schedule the module for execution. The module's execution happens after all other pending
    * events are processed.
    */
   public synchronized void schedule()
   {
      if (!scheduled)
      {
         scheduled = true;

         Daemon.getInstance().invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               Module.this.execute();
               scheduled = false;
            }
         });
      }
   }

   /**
    * A listener that gets informed when the value of an input variable changed.
    */
   private final VariableListener inputVariableChangedListener = new VariableListener()
   {
      @Override
      public void valueChanged(Variable var)
      {
         schedule();
      }
   };
}
