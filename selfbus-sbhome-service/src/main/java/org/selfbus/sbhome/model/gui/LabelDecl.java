package org.selfbus.sbhome.model.gui;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.Namespaces;


/**
 * A label widget.
 */
@XmlType(name = "label", namespace = Namespaces.PROJECT)
public class LabelDecl extends AbstractComponentDecl
{
   @XmlAttribute(name = "text")
   private String text;
   
   @XmlAttribute(name = "icon")
   private String iconName;

   /**
    * @return the label text
    */
   public String getText()
   {
      return text;
   }

   /**
    * @param text - the text to set
    */
   public void setText(String text)
   {
      this.text = text;
   }

   /**
    * @return the iconName
    */
   public String getIconName()
   {
      return iconName;
   }

   /**
    * @param iconName the iconName to set
    */
   public void setIconName(String iconName)
   {
      this.iconName = iconName;
   }
}
