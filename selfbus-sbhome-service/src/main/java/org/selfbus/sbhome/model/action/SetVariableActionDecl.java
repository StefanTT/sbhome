package org.selfbus.sbhome.model.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.Namespaces;

/**
 * An action that sets the value of a variable.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public class SetVariableActionDecl extends AbstractActionDecl
{
   @XmlAttribute
   protected String name;

   @XmlAttribute
   protected String value;

   /**
    * @return The name of the variable.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name of the variable
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Gets the value of the value property.
    * 
    * @return possible object is {@link String }
    */
   public String getValue()
   {
      return value;
   }

   /**
    * Sets the value of the value property.
    * 
    * @param value allowed object is {@link String }
    */
   public void setValue(String value)
   {
      this.value = value;
   }
}
