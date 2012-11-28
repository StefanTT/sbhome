package org.selfbus.sbhome.model.gui.generator;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;
import org.selfbus.sbhome.model.gui.AbstractComponentDecl;
import org.selfbus.sbhome.model.gui.ButtonDecl;
import org.selfbus.sbhome.model.gui.ItemController;
import org.selfbus.sbhome.model.gui.LabelDecl;
import org.selfbus.sbhome.model.gui.PanelDecl;
import org.selfbus.sbhome.model.gui.layout.AbsoluteLayoutDecl;
import org.selfbus.sbhome.model.gui.layout.HorizontalLayoutDecl;
import org.selfbus.sbhome.model.gui.layout.VerticalLayoutDecl;

/**
 * Loop over all items
 */
@XmlType(name = "foreach", namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class Foreach extends AbstractComponentGenerator
{
   @XmlAttribute
   private String var;

   @XmlAttribute
   private String items;

   @XmlElements
   ({
      @XmlElement(name = "label", type = LabelDecl.class),
      @XmlElement(name = "button", type = ButtonDecl.class),
      @XmlElement(name = "foreach", type = Foreach.class),
      @XmlElement(name = "panel", type = PanelDecl.class),
      @XmlElement(name = "itemController", type = ItemController.class),
      @XmlElement(name = "absoluteLayout", type = AbsoluteLayoutDecl.class),
      @XmlElement(name = "horizontalLayout", type = HorizontalLayoutDecl.class),
      @XmlElement(name = "verticalLayout", type = VerticalLayoutDecl.class)
   })
   private List<AbstractComponentDecl> childs;

   /**
    * @return The name of the loop variable
    */
   public String getVar()
   {
      return var;
   }

   /**
    * @return The name of the items to process
    */
   public String getItems()
   {
      return items;
   }

   /**
    * @return The list of children.
    */
   public List<AbstractComponentDecl> getChilds()
   {
      return childs;
   }

   /**
    * Set the list of children.
    * 
    * @param childs - the children to set.
    */
   public void setChilds(List<AbstractComponentDecl> childs)
   {
      this.childs = childs;
   }
}
