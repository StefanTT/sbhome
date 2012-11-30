package org.selfbus.sbhome.model.group;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.freebus.fts.common.address.GroupAddress;
import org.freebus.knxcomm.application.DataPointType;
import org.selfbus.sbhome.model.AbstractIdentified;
import org.selfbus.sbhome.model.Category;
import org.selfbus.sbhome.model.Namespaces;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.GroupListener;

/**
 * A group / communication object
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class Group extends AbstractIdentified
{
   @XmlIDREF
   @XmlAttribute(required = true)
   private Category category;

   @XmlTransient
   private GroupAddress addr;

   private DataPointType dataType;

   @XmlTransient
   private byte[] value;

   @XmlTransient
   private Set<GroupListener> listeners = new CopyOnWriteArraySet<GroupListener>();

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
   @XmlAttribute(name = "addr")
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
    * @return The group data type.
    */
   public DataPointType getDataType()
   {
      return dataType;
   }

   /**
    * Set the group data type.
    * 
    * @param dataType - the data type to set
    */
   public void setDataType(DataPointType dataType)
   {
      this.dataType = dataType;
   }

   /**
    * @return The group data type as string.
    */
   public String getDataTypeStr()
   {
      return dataType.toString().toLowerCase().replace('_', ' ');
   }

   /**
    * Set the group data type.
    * 
    * @param dataType - the data type to set
    */
   @XmlAttribute(name = "dataType", required = true)
   public void setDataTypeStr(String dataType)
   {
      this.dataType = DataPointType.valueOf(dataType.toUpperCase().replace(' ', '_'));
      Validate.notNull(this.dataType, "Invalid data type: " + dataType);
   }

   /**
    * @return The value of the group, or null if the value is unknown.
    */
   public byte[] getValue()
   {
      return value;
   }

   /**
    * Set the value of the group. The value is copied.
    * Fires the {@link #fireValueChanged() value changed} event.
    * 
    * @param value - the value to set, may be null
    */
   public void setValue(byte[] value)
   {
      if (!Arrays.equals(this.value, value))
      {
         this.value = value == null ? null : value.clone();

         if (addr != null && value != null)
            Daemon.getInstance().sendTelegram(addr, dataType, this.value);

         fireValueChanged();
      }
   }

   /**
    * Register a group listener.
    * 
    * @param listener - the listener to register.
    */
   public void addListener(GroupListener listener)
   {
      listeners.add(listener);
   }

   /**
    * Unregister a group listener.
    * 
    * @param listener - the listener to unregister.
    */
   public void removeListener(GroupListener listener)
   {
      listeners.remove(listener);
   }

   /**
    * Fire the {@link GroupListener#groupValueChanged()} event.
    */
   public void fireValueChanged()
   {
      for (GroupListener listener : listeners)
         listener.groupValueChanged(this);
   }
}
