package org.selfbus.sbhome.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractElement
{
   @XmlAttribute
   private String id;

   @XmlAttribute
   private String label;

   /**
    * @return The ID of the item.
    */
   public String getId()
   {
      return id;
   }

   /**
    * @return The human readable label.
    */
   public String getLabel()
   {
      return label;
   }

}
