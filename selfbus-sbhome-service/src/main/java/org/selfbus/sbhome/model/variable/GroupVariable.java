package org.selfbus.sbhome.model.variable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.freebus.fts.common.address.GroupAddress;
import org.selfbus.sbhome.model.Category;
import org.selfbus.sbhome.model.base.Namespaces;
import org.selfbus.sbhome.service.Daemon;

/**
 * A group variable is a {@link Variable} that has an EIB group address. Changing the value of the
 * group variable triggers a group-telegram that is sent to the bus. A group-telegram on the bus
 * changes the value of the group variable.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class GroupVariable extends Variable
{
   private GroupAddress addr;
   private boolean read = true;
   private boolean write = true;

   @XmlIDREF
   @XmlAttribute(required = true)
   private Category category;

   /**
    * @return The category.
    */
   public Category getCategory()
   {
      return category;
   }

   /**
    * Set the category.
    * 
    * @param category - the category to set
    */
   public void setCategory(Category category)
   {
      this.category = category;
   }

   /**
    * @return The group address, or null if undefined
    */
   public GroupAddress getAddr()
   {
      return addr;
   }

   /**
    * Set the group address.
    * 
    * @param address - the group address, may be null
    */
   public void setAddr(GroupAddress addr)
   {
      this.addr = addr;
   }

   /**
    * @return The group address as string in the format "x/y/z".
    */
   @XmlAttribute(name = "addr", required = true)
   public String getAddrStr()
   {
      return addr.toString();
   }

   /**
    * Set the group address with a string in the format "x/y/z".
    * 
    * @param address - the group address string
    */
   public void setAddrStr(String address)
   {
      this.addr = GroupAddress.valueOf(address);
   }

   /**
    * @return True if a group telegram shall set the group variable's value.
    */
   public Boolean isRead()
   {
      return read;
   }

   /**
    * Control if a group telegram shall set the group variable's value.
    * 
    * @param read - the read flag to set
    */
   @XmlAttribute(required = false)
   public void setRead(Boolean read)
   {
      Validate.notNull(read);
      this.read = read;
   }

   /**
    * @return True if changing the value shall trigger a group telegram.
    */
   public Boolean isWrite()
   {
      return write;
   }

   /**
    * Control if changing the value shall trigger a group telegram.
    * 
    * @param write - the write flag to set
    */
   @XmlAttribute(required = false)
   public void setWrite(Boolean write)
   {
      Validate.notNull(write);
      this.write = write;
   }

   /**
    * Set the value of the variable. The value is copied. Fires the {@link #fireValueChanged() value
    * changed} event.
    * 
    * @param value - the value to set, may be null
    */
   @Override
   public void setValue(Object value)
   {
      super.setValue(value);

      if (write)
         Daemon.getInstance().sendTelegram(addr, getType(), getRawValue(), false);
   }
}
