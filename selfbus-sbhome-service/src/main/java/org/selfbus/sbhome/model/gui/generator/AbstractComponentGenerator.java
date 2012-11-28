package org.selfbus.sbhome.model.gui.generator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;
import org.selfbus.sbhome.model.gui.LayoutElement;

/**
 * Abstract base class for generators that generate GUI components. 
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractComponentGenerator implements LayoutElement
{
   @XmlAttribute
   private String id;

   /**
    * @return the ID
    */
   public String getId()
   {
      return id;
   }
}
