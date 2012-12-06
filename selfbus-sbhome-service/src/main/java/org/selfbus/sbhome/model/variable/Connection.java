package org.selfbus.sbhome.model.variable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.Namespaces;

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
}
