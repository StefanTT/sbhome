package org.selfbus.sbhome.designer.actions;

import java.awt.event.ActionEvent;

import org.selfbus.sbhome.designer.internal.I18n;
import org.selfbus.sbhome.designer.misc.ImageCache;

/**
 * Open the settings dialog.
 */
public final class SettingsAction extends BasicAction
{
   private static final long serialVersionUID = -1808547247707505214L;

   /**
    * Create an action object.
    */
   public SettingsAction()
   {
      super(I18n.getMessage("SettingsAction.name"), I18n.getMessage("SettingsAction.toolTip"), ImageCache
            .getIcon("icons/configure"));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
   }
}
