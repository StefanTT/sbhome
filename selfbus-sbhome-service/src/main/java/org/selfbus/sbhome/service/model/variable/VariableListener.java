package org.selfbus.sbhome.service.model.variable;


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
