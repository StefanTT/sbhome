package org.selfbus.sbhome.model.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.base.Namespaces;

/**
 * An action that sets the value of a group.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public class SetGroupValueActionDecl extends AbstractActionDecl
{
   @XmlAttribute(name = "group")
   protected String group;

   @XmlAttribute(name = "value")
   protected String value;

   /**
    * Gets the value of the group property.
    * 
    * @return possible object is {@link String }
    * 
    */
   public String getGroup()
   {
      return group;
   }

   /**
    * Sets the value of the group property.
    * 
    * @param group allowed object is {@link String }
    * 
    */
   public void setGroup(String group)
   {
      this.group = group;
   }

   /**
    * Gets the value of the value property.
    * 
    * @return possible object is {@link String }
    * 
    */
   public String getValue()
   {
      return value;
   }

   /**
    * Sets the value of the value property.
    * 
    * @param value allowed object is {@link String }
    * 
    */
   public void setValue(String value)
   {
      this.value = value;
   }
}
