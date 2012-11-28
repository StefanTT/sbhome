package org.selfbus.sbhome.web.guifactory;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.freebus.fts.common.address.GroupAddress;
import org.freebus.knxcomm.application.GroupValueWrite;
import org.freebus.knxcomm.telegram.Telegram;
import org.selfbus.sbhome.model.Project;
import org.selfbus.sbhome.model.action.AbstractActionDecl;
import org.selfbus.sbhome.model.action.ChangeItemActionDecl;
import org.selfbus.sbhome.model.action.SendTelegramActionDecl;
import org.selfbus.sbhome.model.action.ShowPanelActionDecl;
import org.selfbus.sbhome.model.group.Group;
import org.selfbus.sbhome.model.gui.PanelDecl;
import org.selfbus.sbhome.model.trigger.AbstractTriggerDecl;
import org.selfbus.sbhome.model.trigger.ClickTriggerDecl;
import org.selfbus.sbhome.model.trigger.KeyTriggerDecl;
import org.selfbus.sbhome.model.trigger.TelegramTriggerDecl;
import org.selfbus.sbhome.process.Context;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.service.GroupTelegramListener;
import org.selfbus.sbhome.web.SbHomeApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * A creator that creates triggers.
 */
public class TriggerCreator
{
   private static final Logger LOGGER = LoggerFactory.getLogger(TriggerCreator.class);

   private final Project project;
   private final SbHomeApplication application;
   private final ComponentFactory compFactory;
   private final Evaluator evaluator;

   /**
    * Create a trigger creator.
    * 
    * @param project - the project
    * @param application - the application
    * @param compFactory - the component factory
    */
   public TriggerCreator(Project project, SbHomeApplication application, ComponentFactory compFactory)
   {
      this.project = project;
      this.application = application;
      this.compFactory = compFactory;

      evaluator = new Evaluator(application.getJexl());
   }

   /**
    * Add the triggers to the component. Does not add a trigger that does not contain actions.
    * 
    * @param ctx - the context
    * @param comp - the target component
    * @param triggers - the triggers to add
    */
   public void addTriggers(Context ctx, AbstractComponent comp, List<? extends AbstractTriggerDecl> triggers)
   {
      for (AbstractTriggerDecl trigger : triggers)
         addTrigger(ctx, comp, trigger);
   }

   /**
    * Add a trigger to the component. Does not add the trigger if the trigger does not contain
    * actions.
    * 
    * @param ctx - the context
    * @param comp - the target component
    * @param trigger - the trigger to add
    */
   public void addTrigger(Context ctx, AbstractComponent comp, AbstractTriggerDecl trigger)
   {
      final List<AbstractActionDecl> actions = trigger.getActions();
      if (actions == null || actions.isEmpty())
         return;

      if (trigger instanceof KeyTriggerDecl)
      {
         registerTrigger(ctx, comp, (KeyTriggerDecl) trigger);
      }
      else if (trigger instanceof ClickTriggerDecl)
      {
         registerTrigger(ctx, comp, (ClickTriggerDecl) trigger);
      }
      else if (trigger instanceof TelegramTriggerDecl)
      {
         registerTrigger(ctx, comp, (TelegramTriggerDecl) trigger);
      }
      else
      {
         LOGGER.warn("Ignoring unknown trigger type {}", trigger.getClass().getCanonicalName());
      }
   }

   /**
    * Create a telegram trigger that invokes the supplied actions when triggered.
    * 
    * @param ctx - the context
    * @param comp - the GUI component
    * @param trigger - the trigger
    */
   protected void registerTrigger(final Context ctx, final AbstractComponent comp, final TelegramTriggerDecl trigger)
   {
      final GroupAddress addr = project.getGroup(trigger.getGroup()).getAddr();

      Daemon.getInstance().getEventDispatcher().addTelegramListener(new GroupTelegramListener()
      {
         @Override
         public void telegramReceived(Telegram telegram)
         {
            if (addr.equals(telegram.getDest()) && trigger.matches(telegram))
               performActions(ctx, comp, trigger);
         }
      });
   }

   /**
    * Create a suitable user input trigger that invokes the supplied actions when triggered.
    * 
    * @param ctx - the context
    * @param comp - the GUI component
    * @param trigger - the trigger
    */
   protected void registerTrigger(final Context ctx, final AbstractComponent comp, final ClickTriggerDecl trigger)
   {
      Validate.isTrue(comp instanceof Button, "mouse click triggers can only be added to buttons");
      final Button button = (Button) comp;
      button.addListener(new ClickListener()
      {
         private static final long serialVersionUID = 398072068944530534L;

         @Override
         public void buttonClick(ClickEvent event)
         {
            performActions(ctx, comp, trigger);
         }
      });
   }

   /**
    * Create a suitable user input trigger that invokes the supplied actions when triggered.
    * 
    * @param ctx - the context
    * @param comp - the GUI component
    * @param trigger - the trigger
    */
   protected void registerTrigger(final Context ctx, final AbstractComponent comp, final KeyTriggerDecl trigger)
   {
      final String key = trigger.getKey();

      // TODO handle different input types, not only (left) mouse clicks.
      LOGGER.warn("Ignoring unknown input-trigger key {}", key);
   }

   /**
    * Perform the actions of a trigger.
    * 
    * @param comp - the GUI component
    * @param trigger - the trigger
    */
   public void performActions(final Context ctx, AbstractComponent comp, final AbstractTriggerDecl trigger)
   {
      //LOGGER.info("Performing actions");

      for (final AbstractActionDecl action : trigger.getActions())
         performAction(ctx, comp, action);

      // Sleep a moment to allow the events to be dispatched. This is required to
      // have the GUI show GUI-changing events immediately.
      try
      {
         Thread.sleep(100);
      }
      catch (InterruptedException e)
      {
      }
   }

   /**
    * Perform the actions of a trigger.
    * 
    * @param comp - the GUI component
    * @param action - the action to perform
    */
   public void performAction(final Context ctx, AbstractComponent comp, AbstractActionDecl action)
   {
      if (action instanceof SendTelegramActionDecl)
      {
         final SendTelegramActionDecl sendTelegramAction = (SendTelegramActionDecl) action;

         String groupRef = sendTelegramAction.getGroup();
         Group group = (Group) evaluator.eval(ctx, groupRef);
         Validate.notNull(group, "Invalid group: " + groupRef);

         final GroupAddress dest = group.getAddr();
         final byte[] data = new byte[] { (byte) Integer.parseInt(sendTelegramAction.getValue()) };

         // TODO handle values other than single bytes

         final Telegram telegram = new Telegram(new GroupValueWrite(data));
         telegram.setDest(dest);
         Daemon.getInstance().sendTelegram(telegram);
      }
      else if (action instanceof ChangeItemActionDecl)
      {
         final ChangeItemActionDecl changeItemAction = (ChangeItemActionDecl) action;

         final String label = changeItemAction.getLabel();
         if (label != null)
            comp.setCaption(label);

         final String iconName = changeItemAction.getIcon();
         if (iconName != null)
            comp.setIcon(new ThemeResource("icons/" + iconName + ".png"));
      }
      else if (action instanceof ShowPanelActionDecl)
      {
         ShowPanelActionDecl showPanelAction = (ShowPanelActionDecl) action;

         final PanelDecl decl = project.getPanel(evaluator.evalStr(ctx, showPanelAction.getPanel()));
         application.setContent(compFactory.createPanel(ctx, decl));
      }
      else
      {
         LOGGER.warn("Ignoring action of unknown type {}", action.getClass().getName());
      }
   }
}
