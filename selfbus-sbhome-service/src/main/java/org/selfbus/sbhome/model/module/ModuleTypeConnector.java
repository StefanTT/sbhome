package org.selfbus.sbhome.model.module;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.freebus.knxcomm.application.value.DataPointType;
import org.selfbus.sbhome.base.AbstractNamed;
import org.selfbus.sbhome.base.Namespaces;

/**
 * A connector of a module type 
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ModuleTypeConnector extends AbstractNamed
{
   private DataPointType type;

   /**
    * @return The data type of the variable.
    */
   public DataPointType getType()
   {
      return type;
   }

   /**
    * Set the data type of the variable.
    * 
    * @param type - the data type to set
    */
   public void setType(DataPointType type)
   {
      this.type = type;
   }

   /**
    * @return The data type as string.
    */
   public String getTypeStr()
   {
      return type.toString().toLowerCase().replace('_', ' ');
   }

   /**
    * Set the data type.
    * 
    * @param typeStr - the data type to set as string.
    */
   @XmlAttribute(name = "type", required = false)
   public void setTypeStr(String typeStr)
   {
      DataPointType t = DataPointType.valueOf(typeStr.toUpperCase().replace(' ', '_'));
      Validate.notNull(t, "Variable data type is invalid: " + typeStr);

      setType(t);
   }
}
