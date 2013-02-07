package org.selfbus.sbhome.service.misc;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.selfbus.sbhome.service.model.base.Identified;
import org.selfbus.sbhome.service.model.base.Labeled;
import org.selfbus.sbhome.service.model.base.Named;

/**
 * Utility methods for sorting.
 */
public class SortUtils
{
   /**
    * Sort the items by label.
    * 
    * @param items - the items to sort.
    * @return The items, sorted by label.
    */
   public static <E extends Labeled> List<E> sortByLabel(Collection<E> items)
   {
      Map<String, E> sorter = new TreeMap<String, E>();

      for (E item : items)
         sorter.put(item.getLabel(), item);

      List<E> result = new Vector<E>(items.size());
      result.addAll(sorter.values());

      return result;
   }

   /**
    * Sort the items by ID.
    * 
    * @param items - the items to sort.
    * @return The items, sorted by ID.
    */
   public static <E extends Identified> List<E> sortById(Collection<E> items)
   {
      Map<String, E> sorter = new TreeMap<String, E>();

      for (E item : items)
         sorter.put(item.getId(), item);

      List<E> result = new Vector<E>(items.size());
      result.addAll(sorter.values());

      return result;
   }

   /**
    * Sort the items by name.
    * 
    * @param items - the items to sort.
    * @return The items, sorted by name.
    */
   public static <E extends Named> List<E> sortByName(Collection<E> items)
   {
      Map<String, E> sorter = new TreeMap<String, E>();

      for (E item : items)
         sorter.put(item.getName(), item);

      List<E> result = new Vector<E>(items.size());
      result.addAll(sorter.values());

      return result;
   }
}
