package org.selfbus.sbhome.designer.misc;

/**
 * A generic FTS exception.
 */
public class SbHomeException extends Exception
{
   private static final long serialVersionUID = 249956496800905100L;

   /**
    * Create an empty exception.
    */
   public SbHomeException()
   {
   }

   /**
    * Create an exception.
    * 
    * @param message - the message.
    */
   public SbHomeException(String message)
   {
      super(message);
   }

   /**
    * Create an exception.
    * 
    * @param cause - the cause for the exception.
    */
   public SbHomeException(Throwable cause)
   {
      super(cause);
   }

   /**
    * Create an exception.
    * 
    * @param message - the message.
    * @param cause - the cause for the exception.
    */
   public SbHomeException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
