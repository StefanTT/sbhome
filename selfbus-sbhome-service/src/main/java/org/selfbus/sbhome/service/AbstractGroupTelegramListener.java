package org.selfbus.sbhome.service;

import org.freebus.knxcomm.telegram.Telegram;

/**
 * Abstract base class for group-telegram listeners.
 * The implemented methods do nothing.
 */
public class AbstractGroupTelegramListener implements GroupTelegramListener
{
   /**
    * {@inheritDoc}
    */
   @Override
   public void telegramReceived(Telegram telegram)
   {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void telegramSent(Telegram telegram)
   {
   }
}
