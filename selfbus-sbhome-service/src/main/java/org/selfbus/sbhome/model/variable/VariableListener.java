package org.selfbus.sbhome.model.variable;


/**
 * Interface for listeners for events of a {@link Variable}.
 */
public interface VariableListener
{
   /**
    * The value of the variable changed.
    *
    * @param var - the variable that changed.
    */
   public void valueChanged(Variable var);
}
