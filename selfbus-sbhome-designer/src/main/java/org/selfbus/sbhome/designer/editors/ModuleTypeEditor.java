package org.selfbus.sbhome.designer.editors;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.selfbus.sbhome.service.model.module.ModuleType;

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

   /**
    * Create a module type editor.
    */
   public ModuleTypeEditor()
   {
      textArea = new RSyntaxTextArea();
      add(textArea);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setObject(Object obj)
   {
      moduleType = (ModuleType) obj;

      textArea.setText(moduleType.getCode());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Object getObject()
   {
      return moduleType;
   }
}
