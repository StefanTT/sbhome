package org.selfbus.sbhome.model.gui.layout;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.base.Namespaces;
import org.selfbus.sbhome.model.gui.AbstractComponentDecl;
import org.selfbus.sbhome.model.gui.ButtonDecl;
import org.selfbus.sbhome.model.gui.ItemController;
import org.selfbus.sbhome.model.gui.LabelDecl;
import org.selfbus.sbhome.model.gui.LayoutElement;
import org.selfbus.sbhome.model.gui.PanelDecl;
import org.selfbus.sbhome.model.gui.TelegramHistoryDecl;
import org.selfbus.sbhome.model.gui.generator.Foreach;


/**
 * Abstract base class for layout elements.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlRootElement(name = "panel")
public abstract class AbstractLayoutDecl extends AbstractComponentDecl
{
   @XmlElements
   ({
      @XmlElement(name = "label", type = LabelDecl.class),
      @XmlElement(name = "button", type = ButtonDecl.class),
      @XmlElement(name = "foreach", type = Foreach.class),
      @XmlElement(name = "panel", type = PanelDecl.class),
      @XmlElement(name = "itemController", type = ItemController.class),
      @XmlElement(name = "telegramHistory", type = TelegramHistoryDecl.class),
      @XmlElement(name = "absoluteLayout", type = AbsoluteLayoutDecl.class),
      @XmlElement(name = "horizontalLayout", type = HorizontalLayoutDecl.class),
      @XmlElement(name = "verticalLayout", type = VerticalLayoutDecl.class)
   })
   private List<LayoutElement> childs;

   @XmlAttribute
   private String backgroundImage;

   /**
    * @return The list of children.
    */
   public List<LayoutElement> getChilds()
   {
      return childs;
   }

   /**
    * Set the list of children.
    *
    * @param childs - the children to set.
    */
   public void setChilds(List<LayoutElement> childs)
   {
      this.childs = childs;
   }

   /**
    * @return The name of the background image.
    */
   public String getBackgroundImage()
   {
      return backgroundImage;
   }

   /**
    * Set the name of the background image.
    *
    * @param background - the name of the background image.
    */
   public void setBackgroundImage(String backgroundImage)
   {
      this.backgroundImage = backgroundImage;
   }
}
