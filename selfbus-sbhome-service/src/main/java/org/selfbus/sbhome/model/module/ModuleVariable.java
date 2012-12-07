package org.selfbus.sbhome.model.module;

import org.freebus.knxcomm.application.value.DataPointType;
import org.selfbus.sbhome.model.variable.Variable;
import org.selfbus.sbhome.model.variable.VariableDeclaration;

/**
 * A variable for {@link Module}s.
 */
class ModuleVariable extends Variable
{
   private boolean triggerAlways;

   /**
    * Create an empty variable.
    */
   public ModuleVariable()
   {
      super();
   }

   /**
    * Create a variable.
    * 
    * @param name - the name.
    * @param type - the type.
    */
   public ModuleVariable(String name, DataPointType type)
   {
      super(name, type);
   }

   /**
    * Create a variable.
    * 
    * @param decl - the declaration.
    */
   public ModuleVariable(VariableDeclaration decl)
   {
      super(decl);
   }
   /**
    * @return True if setting the variable value shall always trigger an execution of the module.
    */
   public boolean isTriggerAlways()
   {
      return triggerAlways;
   }

   /**
    * Set if setting the variable value shall always trigger an execution of the module.
    * 
    * @param enable - always execute if true.
    */
   public void setTriggerAlways(boolean enable)
   {
      this.triggerAlways = enable;
   }

   /**
    * @return The regular expression for testing names.
    */
   @Override
   protected String getNameRegex()
   {
      return "^[a-zA-Z_][\\.\\w]*$";
   }
}