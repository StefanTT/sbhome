package org.selfbus.sbhome.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;

/**
 * Base class for objects that have a name.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractNamed implements Named
{
   protected String name;

   /**
    * @return The name of the object.
    */
   @XmlAttribute
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Set the name.
    * 
    * @param name - the name to set.
    */
   public void setName(String name)
   {
      Validate.isTrue(name.matches(getNameRegex()), "Name \"" + name + "\" contains invalid characters");
      this.name = name;
   }

   /**
    * @return The regular expression for testing names.
    */
   protected String getNameRegex()
   {
      return "^[a-zA-Z_][\\w]*$";
   }
}
