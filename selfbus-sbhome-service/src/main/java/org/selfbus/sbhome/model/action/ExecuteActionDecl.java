package org.selfbus.sbhome.model.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.Namespaces;

/**
 * An action that executes a module.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public class ExecuteActionDecl extends AbstractActionDecl
{
   @XmlAttribute
   protected String module;

   /**
    * @return The name of the module.
    */
   public String getModule()
   {
      return module;
   }

   /**
    * Set the name of the module.
    *
    * @param module - the name of the module.
    */
   public void setModule(String module)
   {
      this.module = module;
   }
}
