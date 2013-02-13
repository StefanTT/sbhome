package org.selfbus.sbhome.designer.editors;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.selfbus.sbhome.designer.internal.I18n;
import org.selfbus.sbhome.service.model.module.ModuleType;
import org.selfbus.sbhome.service.model.module.ModuleTypeInputConnector;
import org.selfbus.sbhome.service.model.module.ModuleTypeOutputConnector;
import org.selfbus.sbhome.service.model.variable.VariableDeclaration;

/**
 * An editor component for {@link ModuleType module types}.
 * 
 * See http://fifesoft.com/rsyntaxtextarea
 */
public class ModuleTypeEditor extends AbstractEditor
{
   private static final long serialVersionUID = -1621863608512859347L;

   private ModuleType moduleType;

   private final RSyntaxTextArea textArea;
   private final ModuleTypeEditorParser parser;
   private final Insets defaultInsets = new Insets(2, 2, 2, 2);
   private final JTable inputTable, outputTable, varsTable;
   private final JLabel caption;

   /**
    * Create a module type editor.
    */
   public ModuleTypeEditor()
   {
      setLayout(new GridBagLayout());
      int gridy = 0;

      caption = new JLabel(I18n.formatMessage("ModuleTypeEditor.caption", "..."));
      Font captionFont = getFont().deriveFont((float) (getFont().getSize() * 1.2)).deriveFont(Font.BOLD);
      caption.setFont(captionFont);
      add(caption, new GridBagConstraints(0, gridy++, 3, 1, 3, 0, GridBagConstraints.NORTH,
         GridBagConstraints.HORIZONTAL, defaultInsets, 0, 0));

      inputTable = createVarsTable(0, gridy, I18n.getMessage("ModuleTypeEditor.inputVars"));
      outputTable = createVarsTable(1, gridy, I18n.getMessage("ModuleTypeEditor.outputVars"));
      varsTable = createVarsTable(2, gridy, I18n.getMessage("ModuleTypeEditor.localVars"));
      gridy += 2;

      textArea = new RSyntaxTextArea();
      textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
      textArea.setBracketMatchingEnabled(true);
      textArea.setAutoIndentEnabled(true);
      textArea.setClearWhitespaceLinesEnabled(true);

      parser = new ModuleTypeEditorParser(textArea);
      textArea.addParser(parser);

      RTextScrollPane textScrollPane = new RTextScrollPane(textArea);
      textScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      textScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      textScrollPane.setIconRowHeaderEnabled(true);

      add(textScrollPane, new GridBagConstraints(0, gridy++, 3, 1, 3, 3, GridBagConstraints.NORTH,
         GridBagConstraints.BOTH, defaultInsets, 0, 0));
   }

   /**
    * Create a table for variables.
    * 
    * @param gridx - the X position in the layout grid
    * @param gridy - the Y position in the layout grid
    * @param caption - the caption of the table
    * 
    * @return The created table.
    */
   protected JTable createVarsTable(int gridx, int gridy, String caption)
   {
      JLabel tableCaption = new JLabel(caption);
      add(tableCaption, new GridBagConstraints(gridx, gridy, 1, 1, 1, 0, GridBagConstraints.NORTH,
         GridBagConstraints.HORIZONTAL, defaultInsets, 0, 0));

      JTable table = new JTable(new DefaultTableModel());
      table.setTableHeader(null);
      table.setShowVerticalLines(false);
      table.setDoubleBuffered(false);

      JScrollPane scrollPane = new JScrollPane(table);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setColumnHeaderView(null);

      add(scrollPane, new GridBagConstraints(gridx, gridy + 1, 1, 1, 1, 1, GridBagConstraints.NORTH,
         GridBagConstraints.BOTH, defaultInsets, 0, 0));

      return table;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setObject(Object obj)
   {
      moduleType = (ModuleType) obj;

      parser.setModuleType(moduleType);
      textArea.setText(moduleType.getCode());
      initVarTables();

      caption.setText(I18n.formatMessage("ModuleTypeEditor.caption", moduleType.getName()));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Object getObject()
   {
      return moduleType;
   }

   /**
    * Initialize the variable tables from the module type.
    */
   protected void initVarTables()
   {
      DefaultTableModel inputModel = (DefaultTableModel) inputTable.getModel();
      inputModel.setRowCount(0);
      inputModel.setColumnCount(2);

      DefaultTableModel outputModel = (DefaultTableModel) outputTable.getModel();
      outputModel.setRowCount(0);
      outputModel.setColumnCount(2);

      DefaultTableModel varsModel = (DefaultTableModel) varsTable.getModel();
      varsModel.setRowCount(0);
      varsModel.setColumnCount(2);

      Set<String> names = new TreeSet<String>();
      names.addAll(moduleType.getDeclarationNames());

      for (String name : names)
      {
         VariableDeclaration decl = moduleType.getDeclaration(name);
         String[] row = new String[] { name, decl.getType().toString() };

         if (decl instanceof ModuleTypeInputConnector)
         {
            inputModel.addRow(row);
         }
         else if (decl instanceof ModuleTypeOutputConnector)
         {
            outputModel.addRow(row);
         }
         else
         {
            varsModel.addRow(row);
         }
      }
   }
}
