package org.selfbus.sbhome.designer.misc;

/**
 * A {@link RuntimeException} of FTS.
 */
public class SbHomeRuntimeException extends RuntimeException
{
   private static final long serialVersionUID = 1311874561716525815L;

   /**
    * Create an empty exception.
    */
   public SbHomeRuntimeException()
   {
      super();
   }

   /**
    * Create an exception.
    * 
    * @param message - the message.
    */
   public SbHomeRuntimeException(String message)
   {
      super(message);
   }

   /**
    * Create an exception.
    * 
    * @param cause - the cause for the exception.
    */
   public SbHomeRuntimeException(Throwable cause)
   {
      super(cause);
   }

   /**
    * Create an exception.
    * 
    * @param message - the message.
    * @param cause - the cause for the exception.
    */
   public SbHomeRuntimeException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
