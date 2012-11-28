package org.selfbus.sbhome.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * An item.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class Item extends AbstractElement
{
   @XmlAttribute
   private String group;

   /**
    * @return The ID of the group.
    */
   public String getGroup()
   {
      return group;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getId()
   {
      return group;
   }
}
