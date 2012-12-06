package org.selfbus.sbhome.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jexl2.JexlContext;

/**
 * A Jexl context that can have a parent context, similar to Java's Properties class.
 */
public class Context implements JexlContext
{
   private final Map<String, Object> map;
   private final Context parent;

   /**
    * Create a properties context.
    */
   public Context()
   {
      this(null);
   }

   /**
    * Create a properties context.
    * 
    * @param parent - the parent context
    */
   public Context(Context parent)
   {
      this.parent = parent;
      this.map = new HashMap<String, Object>();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Object get(String name)
   {
      if (map.containsKey(name))
         return map.get(name);

      if (parent != null)
         return parent.get(name);

      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void set(String name, Object value)
   {
      map.put(name, value);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean has(String name)
   {
      if (map.containsKey(name))
         return true;

      if (parent != null)
         return parent.has(name);

      return false;
   }

   /**
    * @return The parent context.
    */
   public Context getParent()
   {
      return parent;
   }

   /**
    * @return All keys that the context contains, excluding the parent context(s).
    */
   public Set<String> localKeySet()
   {
      return map.keySet();
   }
}
