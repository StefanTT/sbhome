package org.selfbus.sbhome.model.gui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;
import org.selfbus.sbhome.model.gui.layout.VerticalLayoutDecl;


/**
 * A panel is a rectangular GUI area that may contain other widgets.
 */
@XmlType(name = "panel", namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class PanelDecl extends VerticalLayoutDecl
{
   @XmlAttribute
   private String title;

   /**
    * @return the title
    */
   public String getTitle()
   {
      return title;
   }

   /**
    * @param title the title to set
    */
   public void setTitle(String title)
   {
      this.title = title;
   }
}
