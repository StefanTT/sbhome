package org.selfbus.sbhome.model.group;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.freebus.fts.common.address.GroupAddress;
import org.selfbus.sbhome.model.Namespaces;
import org.selfbus.sbhome.service.GroupListener;

/**
 * A KNX/EIB group address.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class Group
{
   @XmlAttribute
   private String id;

   @XmlAttribute
   private String category;

   @XmlTransient
   private GroupAddress addr;

   @XmlAttribute(name = "dataType")
   private DataType dataType;

   @XmlTransient
   private byte[] value;

   @XmlTransient
   private Set<GroupListener> listeners = new CopyOnWriteArraySet<GroupListener>();

   /**
    * @return The ID of the group.
    */
   public String getId()
   {
      return id;
   }

   /**
    * Set the ID of the group.
    *
    * @param id - the ID to set
    */
   public void setId(String id)
   {
      this.id = id;
   }

   /**
    * @return The ID of the category.
    */
   public String getCategory()
   {
      return category;
   }

   /**
    * Set the ID of the category.
    *
    * @param category - the category to set
    */
   public void setCategory(String category)
   {
      this.category = category;
   }

   /**
    * @return The group address.
    */
   public GroupAddress getAddr()
   {
      return addr;
   }

   /**
    * Set the group address.
    * 
    * @param address - the group address
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
   public DataType getDataType()
   {
      return dataType;
   }

   /**
    * Set the group data type.
    * 
    * @param dataType - the data type to set
    */
   public void setDataType(DataType dataType)
   {
      this.dataType = dataType;
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
      this.value = value.clone();
      fireValueChanged();
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
