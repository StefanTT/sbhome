package org.selfbus.sbhome.web.guifactory.control;

import org.selfbus.sbhome.model.Item;
import org.selfbus.sbhome.model.gui.ItemController;
import org.selfbus.sbhome.model.variable.Variable;
import org.selfbus.sbhome.model.variable.VariableListener;
import org.selfbus.sbhome.process.Context;
import org.selfbus.sbhome.web.guifactory.Evaluator;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * A read-only control for generic group values.
 */
public class GenericControl extends AbstractControl
{
   private final AbstractLayout component;
   private final Label valueLabel = new Label();

   /**
    * Create a boolean value control.
    */
   public GenericControl(Context ctx, ItemController itemController, Item item, Variable group, Evaluator evaluator)
   {
      super();

      component = new HorizontalLayout();
      component.setSizeFull();

      String label = evaluator.evalStr(ctx, itemController.getLabel());
      if (label == null)
         label = item.getLabel();

      Label nameLabel = new Label(label);
      nameLabel.setWidth(200, Sizeable.UNITS_PIXELS);
      component.addComponent(nameLabel);

      Object value = group.getValue();
      valueLabel.setCaption(value == null ? "" : value.toString());

      component.addComponent(valueLabel);

      group.addListener(new VariableListener()
      {
         @Override
         public void valueChanged(Variable group)
         {
            Object value = group.getValue();
            valueLabel.setCaption(value == null ? "" : value.toString());
         }
      });
   }

   /**
    * @return The GUI component of the controller.
    */
   public AbstractComponent getComponent()
   {
      return component;
   }
}
