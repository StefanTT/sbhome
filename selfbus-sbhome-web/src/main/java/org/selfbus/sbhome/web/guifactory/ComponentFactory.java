package org.selfbus.sbhome.web.guifactory;

import java.util.List;

import org.selfbus.sbhome.model.Project;
import org.selfbus.sbhome.model.gui.AbstractComponentDecl;
import org.selfbus.sbhome.model.gui.ButtonDecl;
import org.selfbus.sbhome.model.gui.ItemController;
import org.selfbus.sbhome.model.gui.LabelDecl;
import org.selfbus.sbhome.model.gui.LayoutElement;
import org.selfbus.sbhome.model.gui.PanelDecl;
import org.selfbus.sbhome.model.gui.generator.Foreach;
import org.selfbus.sbhome.model.gui.layout.AbsoluteLayoutDecl;
import org.selfbus.sbhome.model.gui.layout.AbstractLayoutDecl;
import org.selfbus.sbhome.model.gui.layout.HorizontalLayoutDecl;
import org.selfbus.sbhome.model.gui.layout.VerticalLayoutDecl;
import org.selfbus.sbhome.process.Context;
import org.selfbus.sbhome.web.SbHomeApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Factory class for creating Vaadin components from FB-Home component declarations.
 */
public class ComponentFactory
{
   private static final Logger LOGGER = LoggerFactory.getLogger(ComponentFactory.class);

   private final Project project;
   private final SbHomeApplication application;
   private final Evaluator evaluator;
   private final ForeachCreator foreachCreator;
   private final TriggerCreator triggerCreator;
   private final ItemCreator itemCreator;

   /**
    * Create a component factory.
    * 
    * @param project - the project for which components will be created.
    * @param application - the application
    * @param evaluator- the evaluator to use
    */
   public ComponentFactory(Project project, SbHomeApplication application, Evaluator evaluator)
   {
      this.project = project;
      this.application = application;
      this.evaluator = evaluator;

      foreachCreator = new ForeachCreator(project, this);
      triggerCreator = new TriggerCreator(project, application, this);
      itemCreator = new ItemCreator(project, this, evaluator);
   }

   /**
    * Create a top level panel.
    * 
    * @param decl - the panel declaration
    * @return The created panel.
    */
   public AbstractComponent createPanel(PanelDecl decl)
   {
      return createPanel(new Context(), decl);
   }

   /**
    * Create a panel.
    * 
    * @param parentCtx - the context
    * @param decl - the panel declaration
    * @return The created panel.
    */
   public AbstractComponent createPanel(Context parentCtx, PanelDecl decl)
   {
      Panel panel = new Panel(evaluator.evalStr(parentCtx, decl.getTitle()));

      Context ctx = new Context(parentCtx);
      ctx.set("panel", panel);

      List<LayoutElement> childs = decl.getChilds();
      if (childs.size() == 1 && childs.get(0) instanceof AbstractLayoutDecl)
      {
         panel.setContent((ComponentContainer) createComponent(ctx, childs.get(0)));
      }
      else
      {
         final AbstractLayout layout = new VerticalLayout();
         panel.setContent(layout);

         createComponents(ctx, childs, new ComponentProcessor()
         {
            @Override
            public void process(Context ctx, AbstractComponent comp, LayoutElement decl)
            {
               layout.addComponent(comp);
            }
         });
      }

      return panel;
   }

   /**
    * Create a component for every element of <code>elems</code>.
    * 
    * @param ctx - the context
    * @param elems - the element declarations to process
    * @param layout - the layout to which the created components are added
    */
   public void createComponents(Context ctx, List<LayoutElement> elems, final AbstractLayout layout)
   {
      createComponents(ctx, elems, new ComponentProcessor()
      {
         @Override
         public void process(Context ctx, AbstractComponent comp, LayoutElement decl)
         {
            layout.addComponent(comp);
         }
      });
   }

   /**
    * Create a component for every element of <code>elems</code> and call a processor with the
    * created components.
    * 
    * @param ctx - the context
    * @param elems - the element declarations to process
    * @param proc - the processor to call with each created component
    */
   public void createComponents(Context ctx, List<LayoutElement> elems, ComponentProcessor proc)
   {
      for (LayoutElement elem : elems)
         createComponent(ctx, elem, proc);
   }

   /**
    * Create a component.
    * 
    * @param ctx - the context
    * @param elem - the element declaration
    * @param proc - the processor to call with each created component (may be null)
    * @return The created element, or null if the creation failed
    */
   public AbstractComponent createComponent(Context ctx, LayoutElement elem)
   {
      return createComponent(ctx, elem, null);
   }

   /**
    * Create a component.
    * 
    * @param ctx - the context
    * @param elem - the element declaration
    * @param proc - the processor to call with each created component (may be null)
    * @return The created element, or null if the creation failed
    */
   public AbstractComponent createComponent(Context ctx, LayoutElement elem, ComponentProcessor proc)
   {
      AbstractComponent comp = null;

      if (elem instanceof Foreach)
      {
         if (proc != null)
         {
            foreachCreator.createElements(ctx, (Foreach) elem, proc);
         }
         else
         {
            final AbstractLayout layout = new VerticalLayout();
            comp = layout;

            foreachCreator.createElements(ctx, (Foreach) elem, new ComponentProcessor()
            {
               @Override
               public void process(Context ctx, AbstractComponent comp, LayoutElement decl)
               {
                  layout.addComponent(comp);

                  if (decl instanceof AbstractComponentDecl)
                     addTriggers(ctx, comp, (AbstractComponentDecl) decl);
               }
            });
         }
      }
      else if (elem instanceof AbstractLayoutDecl)
      {
         comp = createLayout(ctx, (AbstractLayoutDecl) elem);
      }
      else if (elem instanceof PanelDecl)
      {
         return createPanel(ctx, (PanelDecl) elem);
      }
      else if (elem instanceof ButtonDecl)
      {
         comp = createButton(ctx, (ButtonDecl) elem);
      }
      else if (elem instanceof LabelDecl)
      {
         comp = createLabel(ctx, (LabelDecl) elem);
      }
      else if (elem instanceof ItemController)
      {
         comp = itemCreator.createController(ctx, (ItemController) elem);
      }
      else
      {
         LOGGER.warn("Invalid component type: {}", elem.getClass().getName());
         return null;
      }

      if (comp != null)
      {
         if (proc != null)
            proc.process(ctx, comp, elem);

         if (elem instanceof AbstractComponentDecl)
         {
            applyGeometry(ctx, comp, (AbstractComponentDecl) elem);
            addTriggers(ctx, comp, (AbstractComponentDecl) elem);
         }
      }

      return comp;
   }

   /**
    * Apply the geometry settings to the component
    * 
    * @param ctx - the context.
    * @param comp - the component to process.
    */
   protected void applyGeometry(Context ctx, AbstractComponent comp, AbstractComponentDecl decl)
   {
      Integer val;

      val = decl.getWidth();
      if (val != null)
         comp.setWidth(val, Sizeable.UNITS_PIXELS);

      val = decl.getHeight();
      if (val != null)
         comp.setHeight(val, Sizeable.UNITS_PIXELS);
   }

   /**
    * Add triggers to the component if applicable
    */
   protected void addTriggers(Context ctx, AbstractComponent comp, AbstractComponentDecl decl)
   {
      triggerCreator.addTriggers(ctx, comp, decl.getTriggers());
   }

   /**
    * Create a layout from a layout declaration.
    * 
    * @param ctx - the context
    * @param decl - the layout declaration.
    * 
    * @return The created layout.
    */
   public AbstractLayout createLayout(Context parentCtx, AbstractLayoutDecl decl)
   {
      if (decl instanceof AbsoluteLayoutDecl)
      {
         return createAbsoluteLayout(parentCtx, (AbsoluteLayoutDecl) decl);
      }

      final Class<? extends AbstractComponentDecl> declClass = decl.getClass();
      AbstractLayout layout = null;

      if (declClass == VerticalLayoutDecl.class)
      {
         layout = new VerticalLayout();
      }
      else if (declClass == HorizontalLayoutDecl.class)
      {
         layout = new HorizontalLayout();
      }
      else
      {
         throw new RuntimeException("Unsupported layout class: " + declClass);
      }

      Context ctx = new Context(parentCtx);
      ctx.set("layout", layout);

      Integer val = decl.getWidth();
      ctx.set("width", val);
      if (val != null)
         layout.setWidth(val, Sizeable.UNITS_PIXELS);

      val = decl.getHeight();
      ctx.set("height", val);
      if (val != null)
         layout.setHeight(val, Sizeable.UNITS_PIXELS);

      final AbstractLayout finalLayout = layout;
      createComponents(ctx, decl.getChilds(), new ComponentProcessor()
      {
         @Override
         public void process(Context ctx, AbstractComponent comp, LayoutElement decl)
         {
            finalLayout.addComponent(comp);
         }
      });

      layout.setStyleName(decl.getId());
      return layout;
   }

   /**
    * Create a component for an absolute layout declaration.
    * 
    * @param ctx - the context
    * @param decl - the layout declaration.
    * 
    * @return The created Vaadin element.
    */
   public AbsoluteLayout createAbsoluteLayout(Context parentCtx, AbsoluteLayoutDecl decl)
   {
      final AbsoluteLayout layout = new AbsoluteLayout();
      layout.setStyleName(decl.getId());

      Integer val = decl.getWidth();
      if (val != null)
         layout.setWidth(val, Sizeable.UNITS_PIXELS);

      val = decl.getHeight();
      if (val != null)
         layout.setHeight(val, Sizeable.UNITS_PIXELS);

      Context ctx = new Context(parentCtx);
      ctx.set("layout", layout);
      ctx.set("width", null);
      ctx.set("height", null);

      createComponents(ctx, decl.getChilds(), new ComponentProcessor()
      {
         @Override
         public void process(Context ctx, AbstractComponent comp, LayoutElement decl)
         {
            if (decl instanceof AbstractComponentDecl)
            {
               final String posStr = ((AbstractComponentDecl) decl).getPosStr();
               layout.addComponent(comp, posStr);
            }
         }
      });

      layout.setStyleName(decl.getId());
      return layout;
   }

   /**
    * Create a label.
    * 
    * @param decl - the declaration of the component.
    * @return The created Vaadin element.
    */
   public Label createLabel(Context ctx, final LabelDecl decl)
   {
      final Label label = new Label();

      label.setStyleName(decl.getId());
      label.setCaption(evaluator.evalStr(ctx, decl.getText()));

      final String iconName = decl.getIconName();
      if (iconName != null && !iconName.isEmpty())
         label.setIcon(new ThemeResource("icons/" + iconName + ".png"));

      return label;
   }

   /**
    * Create a button.
    * 
    * @param decl - the declaration of the component.
    * @return The created Vaadin element.
    */
   public Button createButton(Context ctx, final ButtonDecl decl)
   {
      final Button button = new Button();

      button.setStyleName(decl.getId());
      button.setCaption(evaluator.evalStr(ctx, decl.getText()));

      applyProperties(ctx, button);

      return button;
   }

   /**
    * Apply the properties to the component.
    * 
    * @param comp - the component
    * @param ctx - the context to apply
    */
   protected void applyProperties(Context ctx, AbstractComponent comp)
   {
      Integer val = (Integer) ctx.get("width");
      if (val != null)
         comp.setWidth(val, Sizeable.UNITS_PIXELS);

      val = (Integer) ctx.get("height");
      if (val != null)
         comp.setHeight(val, Sizeable.UNITS_PIXELS);
   }
}
