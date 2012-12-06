package org.selfbus.sbhome.model.variable;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.freebus.fts.common.address.GroupAddress;
import org.freebus.knxcomm.application.value.DataPointType;
import org.freebus.knxcomm.application.value.GroupValueUtils;
import org.selfbus.sbhome.model.base.AbstractNamed;
import org.selfbus.sbhome.model.base.Namespaces;
import org.selfbus.sbhome.service.Daemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A variable.
 * 
 * Variables have a value and can have a {@link GroupAddress group address}. When a variable has a
 * group address, setting the variable value triggers a group telegram to be sent. Listeners can
 * register for being notified on value changes of the variable.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class Variable extends AbstractNamed implements VariableListener
{
   private static final Logger LOGGER = LoggerFactory.getLogger(Variable.class);

   private DataPointType type;
   private Object value;
   private Set<VariableListener> listeners = new CopyOnWriteArraySet<VariableListener>();

   /**
    * Create an empty variable.
    */
   public Variable()
   {
   }

   /**
    * Create a variable.
    * 
    * @param name - the name.
    * @param type - the type.
    */
   public Variable(String name, DataPointType type)
   {
      setName(name);
      this.type = type;
   }

   /**
    * Create a variable.
    * 
    * @param decl - the declaration.
    */
   public Variable(VariableDeclaration decl)
   {
      setName(decl.getName());
      type = decl.getType();
   }

   /**
    * @return The data type of the variable.
    */
   public DataPointType getType()
   {
      return type;
   }

   /**
    * Set the data type of the variable.
    * 
    * @param type - the data type to set
    */
   public void setType(DataPointType type)
   {
      this.type = type;

      if (value == null && type.getValueClass() != null)
         value = type.newValueObject();
   }

   /**
    * @return The data type as string.
    */
   public String getTypeStr()
   {
      return type.toString().toLowerCase().replace('_', ' ');
   }

   /**
    * Set the data type.
    * 
    * @param typeStr - the data type to set as string.
    */
   @XmlAttribute(name = "type", required = true)
   public void setTypeStr(String typeStr)
   {
      DataPointType t = DataPointType.valueOf(typeStr.toUpperCase().replace(' ', '_'));
      Validate.notNull(t, "Variable data type is invalid: " + typeStr);

      setType(t);
   }

   /**
    * @return The value of the variable, or null if the value is unknown.
    */
   public Object getValue()
   {
      return value;
   }

   /**
    * Initialize the value. Does not trigger events.
    */
   public void initValue()
   {
      value = type.newValueObject();
   }

   /**
    * Set the value of the variable. The value is copied. Fires the {@link #fireValueChanged() value
    * changed} event.
    * 
    * @param value - the value to set, may be null
    */
   public void setValue(Object value)
   {
      setValue(value, true);
   }

   /**
    * Set the value of the variable. The value is copied. Optionally fires the
    * {@link #fireValueChanged() value changed} event.
    * 
    * @param value - the value to set, may be null
    * @param fireEvents - shall the value-changed events be fired?
    */
   public void setValue(Object value, boolean fireEvents)
   {
      Validate.notNull(value);

      if (value == this.value || value.equals(this.value))
         return;

      LOGGER.debug("{} = {}", name, value);

      Class<?> typeClass = type.getValueClass();
      if (typeClass != null && value.getClass() != typeClass)
      {
         throw new IllegalArgumentException("Cannot assign a " + value.getClass() + " value to the " + getTypeStr()
            + " variable " + getName());
      }

      this.value = value;

      if (fireEvents && !listeners.isEmpty())
      {
         Daemon.getInstance().invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               fireValueChanged();
            }
         });
      }
   }

   /**
    * @return The raw value of the variable as byte array. Returns null if the value is null.
    */
   public byte[] getRawValue()
   {
      if (value == null)
         return null;

      return GroupValueUtils.toBytes(this.value, type);
   }

   /**
    * Set the value from the raw byte data.
    * 
    * @param raw - the raw byte data
    * @param fireEvents - shall the value-changed events be fired?
    */
   public void setRawValue(byte[] raw, boolean fireEvents)
   {
      setValue(GroupValueUtils.fromBytes(raw, type), fireEvents);
   }

   /**
    * Set the value from the string.
    * 
    * @param str - value as string
    */
   public void setStringValue(String str)
   {
      setValue(GroupValueUtils.fromString(str, type));
   }

   /**
    * Register a variable listener.
    * 
    * @param listener - the listener to register.
    */
   public void addListener(VariableListener listener)
   {
      listeners.add(listener);
   }

   /**
    * Unregister a variable listener.
    * 
    * @param listener - the listener to unregister.
    */
   public void removeListener(VariableListener listener)
   {
      listeners.remove(listener);
   }

   /**
    * Fire the {@link VariableListener#groupValueChanged()} event.
    */
   public void fireValueChanged()
   {
      for (VariableListener listener : listeners)
         listener.valueChanged(this);
   }

   /**
    * The value of a variable has changed. Update this variable and fire all variable listeners.
    * 
    * @param var - the variable who'se value has changed
    */
   @Override
   public void valueChanged(Variable var)
   {
      setValue(var.getValue());
   }
}
