package org.selfbus.sbhome.web.guifactory.control;

import org.selfbus.sbhome.model.Item;
import org.selfbus.sbhome.model.gui.ItemController;
import org.selfbus.sbhome.model.variable.Variable;
import org.selfbus.sbhome.model.variable.VariableListener;
import org.selfbus.sbhome.process.Context;
import org.selfbus.sbhome.web.guifactory.Evaluator;
import org.selfbus.sbhome.web.misc.I18n;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Control for boolean group values.
 */
public class BooleanControl extends AbstractControl
{
   private final AbstractLayout component;
   private final Button valueButton = new Button(I18n.getMessage("Button.off"));
   private Boolean value = false;

   /**
    * Create a boolean value control.
    */
   public BooleanControl(Context ctx, ItemController itemController, Item item, final Variable group, Evaluator evaluator)
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

      value = (Boolean) group.getValue();
      if (value == null)
         value = Boolean.FALSE;

      valueButton.addStyleName("switch");
      component.addComponent(valueButton);
      valueButton.addListener(new ClickListener()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void buttonClick(ClickEvent event)
         {
            value = !value;
            group.setValue(value);
         }
      });

      updateUI();

      group.addListener(new VariableListener()
      {
         @Override
         public void valueChanged(Variable group)
         {
            value = (Boolean) group.getValue();
            updateUI();
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

   /**
    * Update the state of the GUI element(s)
    */
   protected void updateUI()
   {
      if (value.equals(valueButton.getValue()))
         return;

      valueButton.setValue(value);

      if (value)
      {
         valueButton.setCaption(I18n.getMessage("Button.on"));
         valueButton.addStyleName("down");
      }
      else
      {
         valueButton.setCaption(I18n.getMessage("Button.off"));         
         valueButton.removeStyleName("down");
      }
   }
}
