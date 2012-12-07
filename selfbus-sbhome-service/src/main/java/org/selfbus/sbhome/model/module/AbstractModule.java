package org.selfbus.sbhome.model.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbhome.model.base.AbstractNamed;
import org.selfbus.sbhome.model.base.Namespaces;
import org.selfbus.sbhome.model.variable.Variable;
import org.selfbus.sbhome.model.variable.VariableDeclaration;
import org.selfbus.sbhome.model.variable.VariableListener;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for modules that have connections. The input connections trigger the
 * execution of the module.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractModule extends AbstractNamed
{
   static final Logger LOGGER = LoggerFactory.getLogger(AbstractModule.class);

   @XmlAttribute(name = "order", required = false)
   private Integer order;

   protected final Map<String, Variable> vars = new HashMap<String, Variable>();
   private boolean scheduled;

   /**
    * Create an abstract module.
    */
   public AbstractModule()
   {
   }

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
    * @return The order for execution.
    */
   public int getOrder()
   {
      return order == null ? 0 : order;
   }

   /**
    * Set the order for execution.
    * 
    * @param order - the order to set
    */
   public void setOrder(int order)
   {
      this.order = order;
   }

   /**
    * @return True if the module is scheduled for execution.
    */
   public boolean isScheduled()
   {
      return scheduled;
   }

   /**
    * Execute the module. Called automatically when the value of an input connection changes.
    */
   public abstract void execute();

   /**
    * @return The regular expression for testing names.
    */
   @Override
   protected String getNameRegex()
   {
      return "^[a-zA-Z_][\\w\\.]*$";
   }

   /**
    * Initialize the variables.
    * 
    * @param decls - the variable declarations.
    */
   protected void setupVariables(Collection<VariableDeclaration> decls)
   {
      final String varNamePrefix = getName() + '.';
      Validate.notNull(decls);

      vars.clear();
      for (VariableDeclaration decl : decls)
      {
         ModuleVariable var = new ModuleVariable(decl);
         var.setName(varNamePrefix + decl.getName());

         vars.put(var.getName(), var);

         if (decl instanceof ModuleTypeInputConnector)
         {
            var.setTriggerAlways(((ModuleTypeInputConnector) decl).isTriggerAlways());
            var.addListener(inputVariableChangedListener);
         }
      }
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
         LOGGER.debug("Scheduling module {}", name);

         Daemon.getInstance().getProcessor()
            .invokeLater(order == null ? Processor.DEFAULT_PRIORITY : order, new Runnable()
            {
               @Override
               public void run()
               {
                  AbstractModule.this.execute();
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
