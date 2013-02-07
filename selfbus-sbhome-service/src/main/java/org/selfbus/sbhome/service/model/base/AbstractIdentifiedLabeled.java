package org.selfbus.sbhome.service.model.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Base class for objects that have an ID and a label.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractIdentifiedLabeled extends AbstractIdentified implements Labeled
{
   @XmlAttribute
   private String label;

   /**
    * @return The human readable label.
    */
   @Override
   public String getLabel()
   {
      return label;
   }

   /**
    * Set the human readable label.
    * 
    * @param label - the label to set.
    */
   public void setLabel(String label)
   {
      this.label = label;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return getLabel();
   }
}
