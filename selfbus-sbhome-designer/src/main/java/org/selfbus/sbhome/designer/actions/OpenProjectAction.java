package org.selfbus.sbhome.designer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

import org.selfbus.sbhome.designer.internal.I18n;
import org.selfbus.sbhome.designer.misc.ImageCache;

/**
 * Open a project.
 */
public final class OpenProjectAction extends BasicAction
{
   private static final long serialVersionUID = -3511780343333517071L;

   /**
    * Create an action object.
    */
   public OpenProjectAction()
   {
      super(I18n.getMessage("OpenProjectAction.name"), I18n.getMessage("OpenProjectAction.toolTip"), ImageCache
            .getIcon("icons/fileopen"));
      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
   }
}
