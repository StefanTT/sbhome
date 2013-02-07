package org.selfbus.sbhome.service.model.gui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * Declaration of a history view that shows the contents of the daemon's telegram history.
 */
@XmlType(name = "telegramHistory", namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class TelegramHistoryDecl extends AbstractComponentDecl
{
   @XmlAttribute(name = "text")
   private String filter;

   /**
    * @return The filter expression, or null if none is set
    */
   public String getFilter()
   {
      return filter;
   }
}
