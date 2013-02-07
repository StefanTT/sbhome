package org.selfbus.sbhome.service.model.trigger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.base.Named;
import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * A trigger that is triggered at regular intervals.
 */
@XmlRootElement(name = "trigger")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public class CronTriggerDecl extends AbstractTriggerDecl implements Named
{
   @XmlAttribute
   private String name;

   @XmlAttribute
   private String expr;

   /**
    * @return The cron expression.
    */
   public String getExpression()
   {
      return expr;
   }

   /**
    * Set the cron expression.
    * 
    * @param expr - the expression to set.
    */
   public void setExpression(String expr)
   {
      this.expr = expr;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return name;
   }
}
