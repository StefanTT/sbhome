package org.selfbus.sbhome.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.AbstractElement;
import org.selfbus.sbhome.model.base.Namespaces;

/**
 * A controller for a variable (or group).
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class Item extends AbstractElement
{
   @XmlAttribute(required = true)
   private String variable;

   /**
    * @return The name of the variable.
    */
   public String getVariable()
   {
      return variable;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getId()
   {
      return variable;
   }
}
