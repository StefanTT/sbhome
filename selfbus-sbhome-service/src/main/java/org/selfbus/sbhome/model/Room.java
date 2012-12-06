package org.selfbus.sbhome.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.AbstractElement;
import org.selfbus.sbhome.model.base.Namespaces;

/**
 * A room, which is used to group items.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class Room extends AbstractElement
{
   @XmlElement(name = "item")
   private List<Item> items;

   /**
    * @return The items in this room
    */
   public List<Item> getItems()
   {
      if (items == null)
         items = new ArrayList<Item>();

      return items;
   }
}
