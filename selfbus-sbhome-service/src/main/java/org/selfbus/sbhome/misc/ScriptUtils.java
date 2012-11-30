package org.selfbus.sbhome.misc;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.introspection.Sandbox;
import org.apache.commons.jexl2.introspection.SandboxUberspectImpl;

/**
 * Utility methods for scripting.
 */
public class ScriptUtils
{
   /**
    * Create a sandboxed Jexl engine.
    *
    * @return The created Jexl engine.
    */
   public static JexlEngine createJexlEngine()
   {
      Sandbox sandbox = new Sandbox();
      sandbox.black("java.lang.System");
      sandbox.black("java.io.File");
      sandbox.black("java.io.OutputStream");
      sandbox.black("java.io.InputStream");

      SandboxUberspectImpl uber = new SandboxUberspectImpl(null, sandbox);
      JexlEngine jexl = new JexlEngine(uber, null, null, null);

      jexl.setCache(256);
      jexl.setLenient(false);
      jexl.setSilent(false);

      return jexl;
   }
}
