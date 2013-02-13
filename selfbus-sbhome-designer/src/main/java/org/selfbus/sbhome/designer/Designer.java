package org.selfbus.sbhome.designer;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.Validate;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;
import org.selfbus.sbhome.designer.components.Accordion;
import org.selfbus.sbhome.designer.components.Dialogs;
import org.selfbus.sbhome.designer.editors.AbstractEditor;
import org.selfbus.sbhome.designer.editors.ModuleTypeEditor;
import org.selfbus.sbhome.designer.internal.I18n;
import org.selfbus.sbhome.designer.misc.LookAndFeelManager;
import org.selfbus.sbhome.designer.window.XmlMenuFactory;
import org.selfbus.sbhome.designer.window.XmlToolBarFactory;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.misc.Config;
import org.selfbus.sbhome.service.model.Project;
import org.selfbus.sbhome.service.model.Room;
import org.selfbus.sbhome.service.model.module.ModuleType;
import org.selfbus.sbhome.service.model.variable.GroupVariable;
import org.selfbus.sbhome.service.model.variable.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The designer's application class.
 */
public class Designer extends SingleFrameApplication
{
   private static final Logger LOGGER = LoggerFactory.getLogger(Designer.class);

   private String configFileName, projectFileName;
   private Accordion accCategories = new Accordion();
   private JSplitPane spMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
   private JPanel pnlEmpty = new JPanel();
   private AbstractEditor editor;

   private JTable tblRooms = new JTable();
   private JTable tblGroups = new JTable();
   private JTable tblVars = new JTable();
   private JTable tblModuleTypes = new JTable();
   private JTable tblLogic = new JTable();
   private Project project;

   /**
    * Start the designer.
    * 
    * @param args - the commandline arguments
    */
   public static void main(String[] args)
   {
      LookAndFeelManager.setDefaultLookAndFeel();
      Application.launch(Designer.class, args);
   }

   /**
    * @return The application object.
    */
   public static Designer getInstance()
   {
      return getInstance(Designer.class);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void initialize(String[] args)
   {
      super.initialize(args);

      this.addPropertyChangeListener("project", new PropertyChangeListener()
      {
         @Override
         public void propertyChange(PropertyChangeEvent evt)
         {
            projectChanged();
         }
      });

      loadConfig();

      tblModuleTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      tblModuleTypes.getSelectionModel().addListSelectionListener(new ListSelectionListener()
      {
         @Override
         public void valueChanged(ListSelectionEvent e)
         {
            if (!e.getValueIsAdjusting())
            {
               Object obj = tblModuleTypes.getModel().getValueAt(e.getLastIndex(), 0);
               if (obj instanceof ModuleType)
                  createEditor(ModuleTypeEditor.class, obj);
            }
         }
      });
   }

   /**
    * Load the configuration.
    */
   protected void loadConfig()
   {
      configFileName = getContext().getLocalStorage().getDirectory().getPath() + File.separator + "user.config";
      try
      {
         Config.getInstance().load(configFileName);
      }
      catch (FileNotFoundException e)
      {
      }
      catch (IOException e)
      {
         Dialogs.showErrorDialog(I18n.formatMessage("Error.read", configFileName));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void shutdown()
   {
      Config cfg = Config.getInstance();

      cfg.put("lastProject", projectFileName);

      try
      {
         cfg.save(configFileName);
      }
      catch (IOException e)
      {
         Dialogs.showErrorDialog(I18n.formatMessage("Error.write", configFileName));
      }

      super.shutdown();
   }

   /**
    * Responsible for starting the application; for creating and showing the initial GUI.
    * 
    * This method is called by the static launch method, subclasses must override it. It runs on the
    * event dispatching thread.
    */
   @Override
   protected void startup()
   {
      FrameView mainView = getMainView();
      JFrame mainFrame = mainView.getFrame();

      mainFrame.setName("sbhome-designer");
      mainFrame.setMinimumSize(new Dimension(800, 600));
      mainFrame.setTitle(I18n.getMessage("Designer.title"));

      String fileName = "sbhome/designer/main-menubar.xml";
      InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
      Validate.notNull(in, "menubar configuration not found: " + fileName);
      mainView.setMenuBar(new XmlMenuFactory(I18n.BUNDLE).createMenuBar(in));

      fileName = "sbhome/designer/main-toolbar.xml";
      in = getClass().getClassLoader().getResourceAsStream(fileName);
      Validate.notNull(in, "toolbar configuration not found: " + fileName);
      mainView.setToolBar(new XmlToolBarFactory().createToolBar(in));

      JScrollPane scpCategories = new JScrollPane(accCategories);
      scpCategories.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scpCategories.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      scpCategories.setMinimumSize(new Dimension(200, 200));

      spMain.setDividerLocation(200);
      spMain.setLeftComponent(scpCategories);
      spMain.setRightComponent(pnlEmpty);
      mainFrame.add(spMain);

      setupAccordion();

      show(mainView);

      setupDaemon();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void ready()
   {
      loadProject(Config.getInstance().getStringValue("lastProject", "example-project.xml"));
   }

   /**
    * Setup the daemon.
    */
   protected void setupDaemon()
   {

   }

   /**
    * Load a project.
    * 
    * @param fileName - the project file to load
    */
   protected void loadProject(final String fileName)
   {
      final InputStream in = getClass().getResourceAsStream('/' + fileName);
      final Project oldProject = project;

      if (in == null)
      {
         Dialogs.showErrorDialog(I18n.formatMessage("Error.fileNotFound", fileName));
         return;
      }

      Daemon.getInstance().invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            try
            {
               LOGGER.debug("Loading project {}", fileName);

               Daemon daemon = Daemon.getInstance();
               daemon.loadProject(fileName);

               project = daemon.getProject();
               projectFileName = fileName;
            }
            catch (Exception e)
            {
               Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.loadProject", fileName));
               return;
            }

            SwingUtilities.invokeLater(new Runnable()
            {
               @Override
               public void run()
               {
                  firePropertyChange("project", oldProject, project);
               }
            });
         }
      });
   }

   /**
    * The active project changed.
    */
   private void projectChanged()
   {
      getMainFrame().setTitle(I18n.formatMessage("Designer.titleWithProject", new File(projectFileName).getName()));

      DefaultTableModel tableModel;
      int row;

      row = -1;
      List<Room> rooms = project.getRooms();
      tableModel = new DefaultTableModel(rooms.size(), 1);
      for (Room obj : rooms)
      {
         tableModel.setValueAt(obj, ++row, 0);
      }
      tblRooms.setModel(tableModel);

      row = -1;
      List<GroupVariable> groupVars = project.getGroupVariables();
      tableModel = new DefaultTableModel(groupVars.size(), 1);
      for (GroupVariable obj : groupVars)
      {
         tableModel.setValueAt(obj, ++row, 0);
      }
      tblGroups.setModel(tableModel);

      row = -1;
      List<Variable> vars = project.getVariables();
      tableModel = new DefaultTableModel(vars.size(), 1);
      for (Variable obj : vars)
      {
         tableModel.setValueAt(obj, ++row, 0);
      }
      tblVars.setModel(tableModel);

      row = -1;
      List<ModuleType> moduleTypes = project.getModuleTypes();
      tableModel = new DefaultTableModel(moduleTypes.size(), 1);
      for (ModuleType obj : moduleTypes)
      {
         tableModel.setValueAt(obj, ++row, 0);
      }
      tblModuleTypes.setModel(tableModel);
   }

   /**
    * Setup the accordion.
    */
   protected void setupAccordion()
   {
      accCategories.addBar(I18n.getMessage("Designer.rooms"), tblRooms);
      accCategories.addBar(I18n.getMessage("Designer.groups"), tblGroups);
      accCategories.addBar(I18n.getMessage("Designer.variables"), tblVars);
      accCategories.addBar(I18n.getMessage("Designer.logic"), tblLogic);
      accCategories.addBar(I18n.getMessage("Designer.moduleTypes"), tblModuleTypes);
   }

   /**
    * Create and show an editor.
    * 
    * @param clazz - the class of the editor to create.
    * @param obj - the object to be edited.
    */
   public synchronized void createEditor(Class<? extends AbstractEditor> clazz, Object obj)
   {
      Validate.notNull(clazz);
      Validate.notNull(obj);

      LOGGER.debug("Editing {} with {}", obj.toString(), clazz.getSimpleName());

      if (editor != null && editor.getClass() == clazz && editor.getObject() == obj)
         return;

      try
      {
         AbstractEditor newEditor = clazz.newInstance();
         newEditor.setObject(obj);

         spMain.setRightComponent(newEditor);
         editor = newEditor;
      }
      catch (InstantiationException | IllegalAccessException e)
      {
         Dialogs.showExceptionDialog(e, I18n.formatMessage("Error.createEditor", clazz.toString()));
      }
   }
}
