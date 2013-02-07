package org.selfbus.sbhome.designer.actions;

import java.awt.event.ActionEvent;

import org.selfbus.sbhome.designer.internal.I18n;
import org.selfbus.sbhome.designer.misc.ImageCache;

/**
 * Open the about dialog.
 */
public final class AboutAction extends BasicAction
{
   private static final long serialVersionUID = -1300904342361399741L;

   /**
    * Create an action object.
    */
   public AboutAction()
   {
      super(I18n.getMessage("AboutAction.name"), null, ImageCache.getIcon("icons/info"));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
   }
}
