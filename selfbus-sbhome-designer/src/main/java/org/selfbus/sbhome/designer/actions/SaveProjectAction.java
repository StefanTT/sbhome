package org.selfbus.sbhome.designer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

import org.selfbus.sbhome.designer.internal.I18n;
import org.selfbus.sbhome.designer.misc.ImageCache;

/**
 * Save the project.
 */
public final class SaveProjectAction extends BasicAction
{
   private static final long serialVersionUID = 4534053250434634928L;

   /**
    * Create an action object.
    */
   public SaveProjectAction()
   {
      super(I18n.getMessage("SaveProjectAction.name"), I18n.getMessage("SaveProjectAction.toolTip"), ImageCache
            .getIcon("icons/filesave"));
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
   }
}
