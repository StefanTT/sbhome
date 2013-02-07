package org.selfbus.sbhome.service.model.trigger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * A trigger that reacts on key presses.
 */
@XmlRootElement(name = "trigger")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public class KeyTriggerDecl extends AbstractTriggerDecl
{
   @XmlAttribute
   private String key;

   /**
    * @return The key or mouse button that triggers.
    */
   public String getKey()
   {
      return key;
   }

   /**
    * Set the key or mouse button that triggers.
    * 
    * @param key - the key to set.
    */
   public void setKey(String key)
   {
      this.key = key;
   }
}
