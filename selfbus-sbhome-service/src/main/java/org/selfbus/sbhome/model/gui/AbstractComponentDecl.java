package org.selfbus.sbhome.model.gui;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;
import org.selfbus.sbhome.model.trigger.AbstractTriggerDecl;
import org.selfbus.sbhome.model.trigger.ClickTriggerDecl;
import org.selfbus.sbhome.model.trigger.KeyTriggerDecl;
import org.selfbus.sbhome.model.trigger.TelegramTriggerDecl;

/**
 * A component is the base class for graphical GUI elements.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractComponentDecl implements LayoutElement
{
   @XmlAttribute(name = "id")
   private String id;

   @XmlAttribute(name = "x")
   private Integer x;

   @XmlAttribute(name = "y")
   private Integer y;

   @XmlAttribute(name = "width")
   private Integer width;

   @XmlAttribute(name = "height")
   private Integer height;

   @XmlElements
   ({
      @XmlElement(name = "onKey", type = KeyTriggerDecl.class),
      @XmlElement(name = "onClick", type = ClickTriggerDecl.class),
      @XmlElement(name = "onTelegram", type = TelegramTriggerDecl.class)
   })
   private List<AbstractTriggerDecl> triggers;

   /**
    * @return the ID
    */
   public String getId()
   {
      return id;
   }

   /**
    * @param id - the ID to set
    */
   public void setId(String id)
   {
      this.id = id;
   }

   /**
    * @return the x
    */
   public Integer getX()
   {
      return x;
   }

   /**
    * @return The x-position as string "left:<x>px;". Returns an empty string if the x-position is
    *         null.
    */
   public String getXStr()
   {
      if (x == null)
         return "";
      return "left:" + x + "px;";
   }

   /**
    * @param x the x to set
    */
   public void setX(Integer x)
   {
      this.x = x;
   }

   /**
    * @return the y
    */
   public Integer getY()
   {
      return y;
   }

   /**
    * @return The y-position as string "top:<y>px;". Returns an empty string if the y-position is
    *         null.
    */
   public String getYStr()
   {
      if (y == null)
         return "";
      return "top:" + y + "px;";
   }

   /**
    * @param y the y to set
    */
   public void setY(Integer y)
   {
      this.y = y;
   }

   /**
    * @return the width
    */
   public Integer getWidth()
   {
      return width;
   }

   /**
    * @return The width as string "width:<width>px;". Returns an empty string if the width is null.
    */
   public String getWidthStr()
   {
      if (width == null)
         return "";
      return "width:" + width + "px;";
   }

   /**
    * @param width the width to set
    */
   public void setWidth(Integer width)
   {
      this.width = width;
   }

   /**
    * @return the height
    */
   public Integer getHeight()
   {
      return height;
   }

   /**
    * @return The height as string "height:<height>px;". Returns an empty string if the height is
    *         null.
    */
   public String getHeightStr()
   {
      if (height == null)
         return "";
      return "height:" + height + "px;";
   }

   /**
    * @param height the height to set
    */
   public void setHeight(Integer height)
   {
      this.height = height;
   }

   /**
    * @return The position of the component as a string in the format "left:<x>px; top:<y>px;". Null
    *         positions are not included.
    */
   public String getPosStr()
   {
      return getXStr() + getYStr();
   }

   /**
    * @return The size of the component as a string in the format
    *         "width:<width>px; height:<height>px;". Null sizes are not included.
    */
   public String getSizeStr()
   {
      return getWidthStr() + getHeightStr();
   }

   /**
    * @return The triggers.
    */
   public List<AbstractTriggerDecl> getTriggers()
   {
      if (triggers == null)
         triggers = new ArrayList<AbstractTriggerDecl>();

      return triggers;
   }
}
