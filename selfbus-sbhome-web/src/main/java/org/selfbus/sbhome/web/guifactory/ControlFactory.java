package org.selfbus.sbhome.web.guifactory;

import org.apache.commons.lang3.Validate;
import org.freebus.knxcomm.application.value.DataPointType;
import org.selfbus.sbhome.service.model.Item;
import org.selfbus.sbhome.service.model.Project;
import org.selfbus.sbhome.service.model.gui.ItemController;
import org.selfbus.sbhome.service.model.variable.Variable;
import org.selfbus.sbhome.service.process.Context;
import org.selfbus.sbhome.web.guifactory.control.BooleanControl;
import org.selfbus.sbhome.web.guifactory.control.GenericControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.AbstractComponent;

/**
 * Factory class that creates GUI elements for {@link Control controls}.
 */
public class ControlFactory
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ControlFactory.class);

   private final Project project;
   private final Evaluator evaluator;

   /**
    * Create a component creator that creates GUI elements for {@link Control controls}.
    * 
    * @param project - the project
    * @param evaluator - the evaluator to use
    */
   public ControlFactory(Project project, Evaluator evaluator)
   {
      this.project = project;
      this.evaluator = evaluator;
   }

   /**
    * Create GUI elements to represent the control.
    * 
    * @param ctx - the context
    * @param itemController - the item controller to process
    * @return The created component.
    */
   public AbstractComponent createController(Context ctx, ItemController itemController)
   {
      String itemRef = itemController.getItem();
      Item item = (Item) evaluator.eval(ctx, itemRef);
      Validate.notNull(item, "Item not found: " + itemRef);

      String groupRef = item.getVariable();
      Variable group = project.getVariable(groupRef);
      Validate.notNull(item, "Group not found: " + groupRef);

      DataPointType dataType = group.getType();
      if (DataPointType.BOOL.equals(dataType))
      {
         BooleanControl controller = new BooleanControl(ctx, itemController, item, group, evaluator);
         return controller.getComponent();
      }
      else
      {
         LOGGER.warn("Invalid group data-type: {}", dataType.toString());
         GenericControl controller = new GenericControl(ctx, itemController, item, group, evaluator);
         return controller.getComponent();
      }
   }
}
