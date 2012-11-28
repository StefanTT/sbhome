package org.selfbus.sbhome.model.program;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;

/**
 * A connector connects program variables to group values.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractProgramConnector
{
   @XmlAttribute
   private String name;

   @XmlAttribute
   private String group;

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return the group
    */
   public String getGroup()
   {
      return group;
   }

   /**
    * @param group the group to set
    */
   public void setGroup(String group)
   {
      this.group = group;
   }
}
