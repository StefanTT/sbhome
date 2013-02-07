package org.selfbus.sbhome.service.model.variable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.freebus.knxcomm.application.value.DataPointType;
import org.selfbus.sbhome.service.model.base.AbstractNamed;
import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * The declaration of a variable (not the variable itself). Used e.g. for module types.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class VariableDeclaration extends AbstractNamed
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
   @XmlAttribute(name = "type", required = true)
   protected String getTypeStr()
   {
      return type.toString().toLowerCase().replace('_', ' ');
   }

   /**
    * Set the data type.
    * 
    * @param typeStr - the data type to set as string.
    */
   protected void setTypeStr(String typeStr)
   {
      DataPointType t = DataPointType.valueOf(typeStr.toUpperCase().replace(' ', '_'));
      Validate.notNull(t, "Variable data type is invalid: " + typeStr);

      setType(t);
   }
}
