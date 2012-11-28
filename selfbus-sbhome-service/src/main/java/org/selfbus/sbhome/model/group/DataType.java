package org.selfbus.sbhome.model.group;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;

/**
 * Types of group values.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlEnum
public enum DataType
{
   // The @XmlEnumValue must be the same as the enum constants but in lower case.
   // Having different names will cause problems, at least when reporting errors.

   /** A 1 bit value. */
   @XmlEnumValue("boolean")
   BOOLEAN,

   /** A 1 byte value. */
   @XmlEnumValue("byte_1")
   BYTE_1;

   /**
    * @return The data type as it appears in the XML
    */
   public String getName()
   {
      return name().toLowerCase();
   }
}
