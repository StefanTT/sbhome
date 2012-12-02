package org.selfbus.sbhome.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;

/**
 * Base class for objects that have an ID.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractIdentified implements Identified
{
   protected String id;

   /**
    * @return The ID of the object.
    */
   @XmlID
   @XmlAttribute
   @Override
   public String getId()
   {
      return id;
   }

   /**
    * Set the ID.
    * 
    * @param id - the ID to set.
    */
   public void setId(String id)
   {
      Validate.isTrue(id.matches("^[a-zA-Z_][\\w\\.]*$"), "ID \"" + id + "\" contains invalid characters");
      this.id = id;
   }
}
