package org.selfbus.sbhome.designer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

import org.jdesktop.application.Application;
import org.selfbus.sbhome.designer.internal.I18n;
import org.selfbus.sbhome.designer.misc.ImageCache;

/**
 * Exit the application.
 */
public final class ExitAction extends BasicAction
{
   private static final long serialVersionUID = 5649188834706980691L;

   /**
    * Create an action object.
    */
   public ExitAction()
   {
      super(I18n.getMessage("ExitAction.name"), I18n.getMessage("ExitAction.toolTip"), ImageCache.getIcon("icons/exit"));

      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
      Application.getInstance().exit();
   }
}
