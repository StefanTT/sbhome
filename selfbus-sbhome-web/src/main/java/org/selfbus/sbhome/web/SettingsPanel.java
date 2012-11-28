package org.selfbus.sbhome.web;


import org.selfbus.sbhome.web.misc.I18n;

import com.vaadin.ui.Panel;

/**
 * The settings panel, for configuration.
 */
public class SettingsPanel extends Panel
{
   private static final long serialVersionUID = -8865864746729204629L;
   
   private final SbHomeApplication application;

   /**
    * Create a settings panel.
    *
    * @param application - the application
    */
   public SettingsPanel(SbHomeApplication application)
   {
      super(I18n.getMessage("SettingsPanel.title"));
      this.application = application;
   }
}
