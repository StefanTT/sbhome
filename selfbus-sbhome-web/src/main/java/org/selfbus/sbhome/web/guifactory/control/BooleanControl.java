package org.selfbus.sbhome.web.guifactory.control;

import org.freebus.fts.common.address.GroupAddress;
import org.freebus.knxcomm.application.GroupValueWrite;
import org.freebus.knxcomm.telegram.Telegram;
import org.selfbus.sbhome.model.Item;
import org.selfbus.sbhome.model.group.Group;
import org.selfbus.sbhome.model.gui.ItemController;
import org.selfbus.sbhome.process.Context;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.GroupListener;
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
   private final GroupAddress groupAddr;
   private Boolean value = false;

   /**
    * Create a boolean value control.
    */
   public BooleanControl(Context ctx, ItemController itemController, Item item, Group group, Evaluator evaluator)
   {
      super();

      groupAddr = group.getAddr();

      component = new HorizontalLayout();
      component.setSizeFull();

      String label = evaluator.evalStr(ctx, itemController.getLabel());
      if (label == null)
         label = item.getLabel();

      Label nameLabel = new Label(label);
      nameLabel.setWidth(200, Sizeable.UNITS_PIXELS);
      component.addComponent(nameLabel);

      byte[] data = group.getValue();
      value = data == null ? false : data[0] != 0;

      valueButton.addStyleName("switch");
      component.addComponent(valueButton);
      valueButton.addListener(new ClickListener()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void buttonClick(ClickEvent event)
         {
            value = !value;
            updateUI();

            byte[] data = new byte[1];
            data[0] = (byte) (value ? 1 : 0);

            final Telegram telegram = new Telegram(new GroupValueWrite(data));
            telegram.setDest(groupAddr);
            Daemon.getInstance().sendTelegram(telegram);
         }
      });

      updateUI();

      group.addListener(new GroupListener()
      {
         @Override
         public void groupValueChanged(Group group)
         {
            value = group.getValue()[0] != 0;
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
