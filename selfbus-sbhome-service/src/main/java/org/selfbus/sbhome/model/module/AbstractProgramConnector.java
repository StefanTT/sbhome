package org.selfbus.sbhome.model.module;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.base.Namespaces;

/**
 * A connector connects program variables to group values.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractProgramConnector
{
   @XmlAttribute
   private String name;

   @XmlAttribute
   private String variable;

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return the name of the variable
    */
   public String getVariable()
   {
      return variable;
   }

   /**
    * @param variable the variable to set
    */
   public void setVariable(String variable)
   {
      this.variable = variable;
   }
}
