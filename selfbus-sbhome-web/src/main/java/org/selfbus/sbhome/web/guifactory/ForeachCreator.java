package org.selfbus.sbhome.web.guifactory;

import java.util.List;
import java.util.Vector;

import org.selfbus.sbhome.model.Category;
import org.selfbus.sbhome.model.Item;
import org.selfbus.sbhome.model.Project;
import org.selfbus.sbhome.model.Room;
import org.selfbus.sbhome.model.group.Group;
import org.selfbus.sbhome.model.gui.AbstractComponentDecl;
import org.selfbus.sbhome.model.gui.generator.Foreach;
import org.selfbus.sbhome.process.Context;

/**
 * A component creator that creates elements for a {@link Foreach} declaration.
 */
public class ForeachCreator
{
   private final Project project;
   private final ComponentFactory compFactory;

   /**
    * Create a component creator that creates elements for a {@link Foreach} declaration.
    * 
    * @param project - the project
    * @param compFactory - the component factory
    */
   public ForeachCreator(Project project, ComponentFactory compFactory)
   {
      this.project = project;
      this.compFactory = compFactory;
   }

   /**
    * Process a {@link Foreach} declaration.
    * 
    * @param ctx - the context
    * @param decl - the {@link Foreach} declaration
    * @param proc - the processor to call with each created component
    */
   public void createElements(Context ctx, Foreach decl, ComponentProcessor proc)
   {
      String type = decl.getItems();
      List<AbstractComponentDecl> childs = decl.getChilds();

      if ("room".equals(type))
      {
         for (Room room : project.getRooms())
         {
            Context childCtx = new Context(ctx);
            childCtx.set("room", room);

            for (AbstractComponentDecl child : childs)
               compFactory.createComponent(childCtx, child, proc);
         }
      }
      else if ("category".equals(type))
      {
         for (Category category : project.getCategories())
         {
            Context childCtx = new Context(ctx);
            childCtx.set("category", category);

            for (AbstractComponentDecl child : childs)
               compFactory.createComponent(childCtx, child, proc);
         }
      }
      else if ("item".equals(type)) // all items of a room or category
      {
         final Room parentRoom = (Room) ctx.get("room");
         final Category parentCategory = (Category) ctx.get("category");

         List<Room> rooms;
         if (parentRoom != null)
         {
            rooms = new Vector<Room>(1);
            rooms.add(parentRoom);
         }
         else rooms = project.getRooms();

         for (Room room : rooms)
         {
            Context roomCtx = new Context(ctx);
            roomCtx.set("room", room);

            for (Item item : room.getItems())
            {
               Context itemCtx = new Context(roomCtx);
               itemCtx.set("item", item);
               itemCtx.set("room", room);

               if (parentCategory == null)
               {
                  for (AbstractComponentDecl child : childs)
                     compFactory.createComponent(itemCtx, child, proc);
               }
               else
               {
                  Group group = project.getGroup(item.getGroup());
                  if (group != null && parentCategory.getId().equals(group.getCategory()))
                  {
                     for (AbstractComponentDecl child : childs)
                        compFactory.createComponent(itemCtx, child, proc);
                  }
               }
            }
         }
      }
      else if ("group".equals(type))
      {
         for (Group group : project.getGroups())
         {
            Context childCtx = new Context(ctx);
            childCtx.set("group", group);

            for (AbstractComponentDecl child : childs)
               compFactory.createComponent(childCtx, child, proc);
         }
      }
      else
      {
         throw new RuntimeException("Invalid foreach item type: " + type);
      }
   }
}
