package org.selfbus.sbhome.model.gui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;

/**
 * A controller for an item. Visualizes the item and allows to manipulate
 * the item.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemController extends AbstractComponentDecl
{
   @XmlAttribute(required = true)
   private String item;

   @XmlAttribute
   private String label;

   /**
    * @return The ID of the controlled item.
    */
   public String getItem()
   {
      return item;
   }

   /**
    * @return The label of the controller, may be null.
    */
   public String getLabel()
   {
      return label;
   }
}
