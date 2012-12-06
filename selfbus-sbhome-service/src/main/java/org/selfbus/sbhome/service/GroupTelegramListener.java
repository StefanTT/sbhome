package org.selfbus.sbhome.service;

import java.util.EventListener;

import org.freebus.knxcomm.telegram.Telegram;

/**
 * A listener that can receive telegram events.
 */
public interface GroupTelegramListener extends EventListener
{
   /**
    * A group {@link Telegram telegram} was received.
    * 
    * @param telegram - the received telegram.
    */
   public void telegramReceived(Telegram telegram);

   /**
    * A group {@link Telegram telegram} was sent.
    * 
    * @param telegram - the received telegram.
    */
   public void telegramSent(Telegram telegram);
}
