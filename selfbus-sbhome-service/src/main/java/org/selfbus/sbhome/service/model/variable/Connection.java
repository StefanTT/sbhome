package org.selfbus.sbhome.service.model.variable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * A directed connection from one variable to another.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class Connection
{
   @XmlAttribute(required = true)
   private String from;

   @XmlAttribute(required = true)
   private String to;

   @XmlAttribute(required = false)
   private Boolean triggerAlways;

   /**
    * @return the from
    */
   public String getFrom()
   {
      return from;
   }

   /**
    * @param from the from to set
    */
   public void setFrom(String from)
   {
      this.from = from;
   }

   /**
    * @return the to
    */
   public String getTo()
   {
      return to;
   }

   /**
    * @param to the to to set
    */
   public void setTo(String to)
   {
      this.to = to;
   }

   /**
    * @return True if the connection shall fire events even if the value did not change.
    */
   public boolean isTriggerAlways()
   {
      return triggerAlways == null ? false : triggerAlways;
   }

   /**
    * Toggle if the connection shall fire events even if the value did not change.
    * 
    * @param enable - always fire events if true.
    */
   public void setTriggerAlways(boolean enable)
   {
      this.triggerAlways = enable;
   }
}
