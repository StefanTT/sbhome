package org.selfbus.sbhome.model.module;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.Namespaces;
import org.selfbus.sbhome.model.variable.VariableDeclaration;

/**
 * An input connector for model types.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleTypeInputConnector extends VariableDeclaration
{
   @XmlAttribute(required = false)
   private Boolean triggerAlways;

   /**
    * @return True if setting the variable value shall always trigger an execution of the module.
    */
   public boolean isTriggerAlways()
   {
      return triggerAlways == null ? false : triggerAlways;
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
}
