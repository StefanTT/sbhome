package org.selfbus.sbhome.misc;

import static org.junit.Assert.*;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.Script;
import org.junit.Test;
import org.selfbus.sbhome.service.misc.ScriptUtils;

public class TestScriptUtils
{
   @Test
   public void createJexlEngineWhite()
   {
      JexlEngine jexl = ScriptUtils.createJexlEngine();
      assertNotNull(jexl);

      String expr = "new('java.lang.String', 'my string')";
      Script script = jexl.createScript(expr);
      script.execute(null);
   }

   @Test(expected = JexlException.class)
   public void createJexlEngineBlack()
   {
      JexlEngine jexl = ScriptUtils.createJexlEngine();
      assertNotNull(jexl);

      String expr = "new('java.io.File', 'myfile.txt')";
      Script script = jexl.createScript(expr);
      script.execute(null);
   }
}
