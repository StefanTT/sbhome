package org.selfbus.sbhome.web.guifactory;

import org.selfbus.sbhome.model.gui.LayoutElement;
import org.selfbus.sbhome.process.Context;

import com.vaadin.ui.AbstractComponent;

/**
 * Interface for component processing classes.
 */
public interface ComponentProcessor
{
   /**
    * Process a component.
    * 
    * @param ctx - the context.
    * @param comp - the component to process.
    * @param decl - the declaration of the component.
    */
   public void process(Context ctx, AbstractComponent comp, LayoutElement decl);
}
