package org.selfbus.sbhome.web.guifactory.component;

import org.freebus.knxcomm.telegram.Telegram;
import org.selfbus.sbhome.model.gui.TelegramHistoryDecl;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.GroupTelegramListener;
import org.selfbus.sbhome.web.misc.I18n;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Table;

/**
 * A high-level component that shows the contents of the daemon's telegram history.
 */
public class TelegramHistory
{
   private final Table table = new Table();
   private int maxHistory = 20;
   private Integer firstItemId = 0;
   private Integer lastItemId = firstItemId - 1;

   /**
    * Create a telegram-history object.
    * 
    * @param decl - the declaration
    */
   public TelegramHistory(TelegramHistoryDecl decl)
   {
      table.addContainerProperty(I18n.getMessage("TelegramHistory.caption"), String.class, "");
      table.setSizeFull();

      Daemon daemon = Daemon.getInstance();
      daemon.addTelegramListener(new GroupTelegramListener()
      {
         @Override
         public void telegramReceived(Telegram telegram)
         {
            addTelegram(telegram);
            table.commit();
         }

         @Override
         public void telegramSent(Telegram telegram)
         {
            addTelegram(telegram);
            table.commit();
         }
      });

      synchronized (table)
      {
         for (Telegram telegram : daemon.getTelegramHistory())
            addTelegram(telegram);
      }

      table.commit();
   }

   /**
    * @return The top-level GUI component of this object.
    */
   public AbstractComponent getComponent()
   {
      return table;
   }

   /**
    * Add a telegram to the end of the table.
    * 
    * @param telegram - the telegram to add
    */
   protected void addTelegram(Telegram telegram)
   {
      synchronized (table)
      {
         while (table.size() > maxHistory)
            table.removeItem(++firstItemId);

         String[] cells = new String[1];
         cells[0] = telegram.toString();

         table.addItem(cells, ++lastItemId);
      }
   }
}
