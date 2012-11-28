package org.selfbus.sbhome.model.trigger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.freebus.knxcomm.application.ApplicationType;
import org.freebus.knxcomm.application.GroupValueWrite;
import org.freebus.knxcomm.telegram.Telegram;
import org.selfbus.sbhome.model.Namespaces;

/**
 * A trigger that triggers when a group telegram for a specific address is received.
 * Optionally the trigger can be restricted to a specific group value too.
 */
@XmlRootElement(name = "trigger")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public class TelegramTriggerDecl extends AbstractTriggerDecl
{
   @XmlAttribute(name = "group")
   protected String group;

   @XmlAttribute(name = "value")
   protected String value;

   /**
    * Set the name of the group
    * 
    * @param group - the group to set
    */
   public void setGroup(String group)
   {
      this.group = group;
   }

   /**
    * @return The name of the group
    */
   public String getGroup()
   {
      return group;
   }

   /**
    * Sets the expected group value.
    * 
    * @param value - the value to set
    */
   public void setValue(String value)
   {
      this.value = value;
   }

   /**
    * @return The expected group value
    */
   public String getValue()
   {
      return value;
   }

   /**
    * Test if the telegram matches the trigger conditions.
    * 
    * @param telegram - the telegram to test
    * @return True if the telegram triggers this trigger.
    */
   public boolean matches(Telegram telegram)
   {
      if (!telegram.getApplicationType().equals(ApplicationType.GroupValue_Write))
         return false;

      final GroupValueWrite app = (GroupValueWrite) telegram.getApplication();
      int val = app.getData()[0] & 255;

      return (value == null || value.isEmpty() || val == Integer.parseInt(value));
   }
}
