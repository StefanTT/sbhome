package org.selfbus.sbhome.web;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.jexl2.JexlEngine;
import org.freebus.fts.common.SimpleConfig;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.misc.ScriptUtils;
import org.selfbus.sbhome.service.model.Project;
import org.selfbus.sbhome.service.model.gui.PanelDecl;
import org.selfbus.sbhome.web.guifactory.ComponentFactory;
import org.selfbus.sbhome.web.guifactory.Evaluator;
import org.selfbus.sbhome.web.misc.I18n;
import org.selfbus.sbhome.web.panels.SettingsPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Base class of the web application. One instance of this class is
 * created per thread (at least).
 */
public class SbHomeApplication extends Application
{
   private static final Logger LOGGER = LoggerFactory.getLogger(SbHomeApplication.class);
   private static final long serialVersionUID = -1821682921589068770L;
   private static final SimpleConfig CONFIG = new SimpleConfig();
   
   private final JexlEngine jexl = ScriptUtils.createJexlEngine();
   private final Evaluator evaluator = new Evaluator(jexl);
   private ComponentFactory componentFactory;
   private AbstractSplitPanel mainSplitPanel;
   private Project project;

   static
   {
      try
      {
         CONFIG.load("sbhome");
      }
      catch (FileNotFoundException e)
      {
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public SbHomeApplication() throws FileNotFoundException, IOException
   {
      LOGGER.info("Application created");
   }

   /**
    * @return The application's {@link Evaluator evaluator}.
    */
   public Evaluator getEvaluator()
   {
      return evaluator;
   }

   /**
    * @return The application's {@link JexlEngine Jexl engine}.
    */
   public JexlEngine getScriptEngine()
   {
      return jexl;
   }

   /**
    * @return The global configuration.
    */
   public static SimpleConfig getConfig()
   {
      return CONFIG;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void init()
   {
      setTheme("sbhome-black");

      final Daemon daemon = new Daemon("example-project.xml");
      project = daemon.getProject();

      componentFactory = new ComponentFactory(project, this, evaluator);

      Window mainWindow = new Window();
      setMainWindow(mainWindow);

      AbstractLayout bodyLayout = new VerticalLayout();
      mainWindow.setContent(bodyLayout);

      AbstractComponentContainer navbar = createNavbar();
      navbar.setWidth(Sizeable.SIZE_UNDEFINED, Sizeable.UNITS_PIXELS);
      bodyLayout.addComponent(navbar);

      mainSplitPanel = new HorizontalSplitPanel();
      mainSplitPanel.setHeight(500, Sizeable.UNITS_PIXELS);
      bodyLayout.addComponent(mainSplitPanel);

      // The hidden progress indicator pulls GUI updates in regular intervals.
      final ProgressIndicator progress = new ProgressIndicator(0.0f);
      progress.setPollingInterval(500);
      progress.setStyleName("invisible");
      mainWindow.addComponent(progress);

      mainWindow.addListener(new CloseListener()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void windowClose(CloseEvent e)
         {
            // The user closed the window or left the page
            LOGGER.info("window closed");

            // TODO unregister all triggers of the closed window
         }
      });

      PanelDecl panelDecl;
      AbstractComponent panel;

      panelDecl = project.getPanel(project.getStartPanel());
      if (panelDecl != null)
      {
         panel = componentFactory.createPanel(null, panelDecl);
         panel.setHeight(500, Sizeable.UNITS_PIXELS);
         mainSplitPanel.addComponent(panel);
      }

      panelDecl = project.getPanel("rightMainPanel");
      if (panelDecl != null)
      {
         panel = componentFactory.createPanel(null, panelDecl);
         panel.setHeight(500, Sizeable.UNITS_PIXELS);
         mainSplitPanel.addComponent(panel);
      }
   }

   /**
    * Set the contents of the content area.
    *
    * @param content - the content to set
    */
   public void setContent(AbstractComponent content)
   {
      content.setHeight(500, Sizeable.UNITS_PIXELS);
      mainSplitPanel.setFirstComponent(content);
   }

   /**
    * Create the main navigation bar.
    */
   protected AbstractComponentContainer createNavbar()
   {
      Button btn;

      HorizontalLayout navbar = new HorizontalLayout();
      navbar.setStyleName("segment");
      navbar.addStyleName("navbar");

      btn = new Button(I18n.getMessage("Button.home"));
      btn.addStyleName("first");
      navbar.addComponent(btn);
      btn.addListener(new ClickListener()
      {
         private static final long serialVersionUID = -1858108757434959138L;

         @Override
         public void buttonClick(ClickEvent event)
         {
            final PanelDecl startDecl = project.getPanel(project.getStartPanel());
            setContent(componentFactory.createPanel(null, startDecl));
         }
      });

      btn = new Button(I18n.getMessage("Button.settings"));
      btn.addStyleName("last");
      navbar.addComponent(btn);
      navbar.setComponentAlignment(btn, Alignment.MIDDLE_RIGHT);
      btn.addListener(new ClickListener()
      {
         private static final long serialVersionUID = -1858108757434959138L;

         @Override
         public void buttonClick(ClickEvent event)
         {
            setContent(new SettingsPanel(SbHomeApplication.this));
         }
      });

      return navbar;
   }
}
