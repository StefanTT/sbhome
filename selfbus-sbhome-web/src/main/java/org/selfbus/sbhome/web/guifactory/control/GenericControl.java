package org.selfbus.sbhome.web.guifactory.control;

import org.freebus.fts.common.HexString;
import org.selfbus.sbhome.model.Item;
import org.selfbus.sbhome.model.group.Group;
import org.selfbus.sbhome.model.gui.ItemController;
import org.selfbus.sbhome.process.Context;
import org.selfbus.sbhome.service.GroupListener;
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
   public GenericControl(Context ctx, ItemController itemController, Item item, Group group, Evaluator evaluator)
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

      byte[] data = group.getValue();
      valueLabel.setCaption(data == null ? "" : HexString.toString(data));

      component.addComponent(valueLabel);

      group.addListener(new GroupListener()
      {
         @Override
         public void groupValueChanged(Group group)
         {
            byte[] data = group.getValue();
            valueLabel.setCaption(data == null ? "" : HexString.toString(data));
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
