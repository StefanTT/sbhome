package org.selfbus.sbhome.model.trigger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.Namespaces;

/**
 * A trigger that triggers when a group value changes.
 */
@XmlRootElement(name = "trigger")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(namespace = Namespaces.PROJECT)
public class ValueChangeTriggerDecl extends AbstractTriggerDecl
{
   @XmlAttribute(name = "group")
   protected String group;

   /**
    * Set the group
    * 
    * @param group - the group to set
    */
   public void setGroup(String group)
   {
      this.group = group;
   }

   /**
    * @return The group
    */
   public String getGroup()
   {
      return group;
   }
}
