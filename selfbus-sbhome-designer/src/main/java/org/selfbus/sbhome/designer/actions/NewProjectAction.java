package org.selfbus.sbhome.designer.actions;

import java.awt.event.ActionEvent;

import org.selfbus.sbhome.designer.internal.I18n;
import org.selfbus.sbhome.designer.misc.ImageCache;

/**
 * Create a project.
 */
public final class NewProjectAction extends BasicAction
{
   private static final long serialVersionUID = 1017643248154809244L;

   /**
    * Create an action object.
    */
   public NewProjectAction()
   {
      super(I18n.getMessage("NewProjectAction.name"), I18n.getMessage("NewProjectAction.toolTip"), ImageCache
         .getIcon("icons/filenew"));
   }

   /**
    * Perform the action.
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
   }
}
