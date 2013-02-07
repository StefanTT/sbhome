package org.selfbus.sbhome.web.guifactory;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;
import org.selfbus.sbhome.service.process.Context;

/**
 * Class for evaluating expressions.
 */
public class Evaluator
{
   private final JexlEngine jexl;

   /**
    * Create an evaluator with a private {@link JexlEngine}.
    */
   public Evaluator()
   {
      jexl = new JexlEngine();
      jexl.setCache(100);
      jexl.setLenient(false);
      jexl.setSilent(false);
   }

   /**
    * Create an evaluator.
    * 
    * @param jexl - the {@link JexlEngine} to use.
    */
   public Evaluator(JexlEngine jexl)
   {
      this.jexl = jexl;
   }

   /**
    * Evaluate a string which might contain an expression, e.g. "The ${room.label} room"
    * or "${room}".
    * 
    * @param ctx - the context
    * @param str - the string to evaluate
    * @return The evaluated object, which might be a string but could also be an object.
    */
   public Object eval(Context ctx, String str)
   {
      if (str == null)
         return null;

      if (str.startsWith("${") && str.endsWith("}"))
         return evalExpr(ctx, str.substring(2, str.length() - 1));

      String[] parts = str.split("\\$\\{");
      if (parts.length == 1)
         return parts[0];

      StringBuilder sb = new StringBuilder();
      sb.append(parts[0]);

      for (int i = 1; i < parts.length; ++i)
      {
         String[] p = parts[i].split("\\}", 2);
         sb.append(evalExpr(ctx, p[0])).append(p[1]);
      }

      return sb.toString();
   }

   /**
    * Evaluate a string which might contain an expression, e.g. "The ${room.label} room"
    * 
    * @param ctx - the context
    * @param str - the string to evaluate
    * @return The evaluated string.
    */
   public String evalStr(Context ctx, String str)
   {
      if (str == null)
         return null;

      String[] parts = str.split("\\$\\{");
      if (parts.length == 1)
         return parts[0];

      StringBuilder sb = new StringBuilder();
      sb.append(parts[0]);

      for (int i = 1; i < parts.length; ++i)
      {
         String[] p = parts[i].split("\\}", 2);
         sb.append(evalExpr(ctx, p[0])).append(p[1]);
      }

      return sb.toString();
   }

   /**
    * Evaluate an expression, e.g. "room.label".
    * 
    * @param ctx - the context for the evaluation
    * @param expr - the expression to evaluate
    * @return The evaluated object
    */
   public Object evalExpr(Context ctx, String expr)
   {
      Expression e = jexl.createExpression(expr);
      return e.evaluate(ctx);
   }

   /**
    * @return The {@link JexlEngine Jex engine}.
    */
   public JexlEngine getJexl()
   {
      return jexl;
   }
}
