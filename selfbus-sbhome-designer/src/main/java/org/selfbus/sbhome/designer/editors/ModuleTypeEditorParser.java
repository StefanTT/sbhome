package org.selfbus.sbhome.designer.editors;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.swing.text.BadLocationException;

import org.apache.commons.jexl2.DebugInfo;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.JexlInfo;
import org.apache.commons.jexl2.Script;
import org.apache.commons.jexl2.parser.ParseException;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.model.module.ModuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parser for the {@link ModuleTypeEditor} that validates the module type
 * that is edited in the editor.
 */
public class ModuleTypeEditorParser extends AbstractParser
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ModuleTypeEditorParser.class);

   private final WeakReference<RSyntaxTextArea> textAreaRef;
   private ModuleType moduleType;

   /**
    * Create a parser.
    *
    * @param textArea - the text area that uses the parser.
    */
   public ModuleTypeEditorParser(RSyntaxTextArea textArea)
   {
      this.textAreaRef = new WeakReference<RSyntaxTextArea>(textArea);
   }

   /**
    * Set the module type.
    * 
    * @param moduleType - the module type
    */
   public void setModuleType(ModuleType moduleType)
   {
      this.moduleType = moduleType;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   synchronized public ParseResult parse(RSyntaxDocument doc, String style)
   {
      DefaultParseResult result = new DefaultParseResult(this);
      result.setParsedLines(0, doc.getLength());

      JexlInfo info = new DebugInfo("code", 0, 0);

      try
      {
         String code = doc.getText(0, doc.getLength());
         Script script = Daemon.getInstance().getScriptEngine().createScript(code, info, null);
         for (List<String> var : script.getVariables())
         {
            String varName = var.get(0);
            if (moduleType.getDeclaration(varName) == null)
            {
               String msg = "Unknown variable: " + varName;
               DefaultParserNotice notice = new DefaultParserNotice(this, msg, 0);
               notice.setLevel(ParserNotice.WARNING);
               notice.setShowInEditor(true);
               notice.setToolTipText(msg);
               result.addNotice(notice);
               LOGGER.warn(msg);
            }
         }
      }
      catch (BadLocationException e)
      {
         // Seems to happen sometimes. Just ignore it for now.
      }
      catch (JexlException e)
      {
         int line, col;
         String msg;

         if (e.getCause() instanceof ParseException)
         {
            ParseException pe = (ParseException) e.getCause();
            line = pe.getLine() - 1;
            col = pe.getColumn() - 1;
            msg = pe.getMessage();
         }
         else
         {
            String[] msgLocParts = e.getMessage().split("[@: ]", 4);
            line = Integer.parseInt(msgLocParts[1]) - 1;
            col = Integer.parseInt(msgLocParts[2]) - 1;
            msg = msgLocParts[3];
         }

         int startOffs = 0;
         int endOffs = doc.getLength();
         try
         {
            startOffs = textAreaRef.get().getLineStartOffset(line) + col;
            endOffs = textAreaRef.get().getLineEndOffset(line);
         }
         catch (BadLocationException e1)
         {
            e1.printStackTrace();
         }

         if (e.getCause() instanceof ParseException)
         {
            ParseException pe = (ParseException) e.getCause();
            endOffs = startOffs + Math.max(1, pe.getAfter().length());
         }

         DefaultParserNotice notice = new DefaultParserNotice(this, msg, line, startOffs, endOffs);
         notice.setLevel(ParserNotice.ERROR);
         notice.setShowInEditor(true);
         notice.setToolTipText(msg);
         result.addNotice(notice);

         LOGGER.warn(msg);
      }

      return result;
   }
}
