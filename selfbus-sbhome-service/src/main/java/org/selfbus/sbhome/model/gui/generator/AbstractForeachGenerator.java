package org.selfbus.sbhome.model.gui.generator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;

/**
 * Abstract base class for generators that generate GUI components with a "foreach" enumeration.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractForeachGenerator extends AbstractComponentGenerator
{
   @XmlAttribute
   private String foreach;

   /**
    * @return the "foreach" specification
    */
   public String getForEach()
   {
      return foreach;
   }
}
