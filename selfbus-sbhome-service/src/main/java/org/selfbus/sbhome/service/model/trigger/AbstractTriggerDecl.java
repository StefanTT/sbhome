package org.selfbus.sbhome.service.model.trigger;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.action.AbstractActionDecl;
import org.selfbus.sbhome.service.model.action.ChangeItemActionDecl;
import org.selfbus.sbhome.service.model.action.SetVariableActionDecl;
import org.selfbus.sbhome.service.model.action.ShowPanelActionDecl;
import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * Abstract base class for triggers.
 */
@XmlRootElement(name = "trigger")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public abstract class AbstractTriggerDecl
{
   @XmlElements
   ({
      @XmlElement(name = "execute", type = ShowPanelActionDecl.class),
      @XmlElement(name = "changeItem", type = ChangeItemActionDecl.class),
      @XmlElement(name = "setVariable", type = SetVariableActionDecl.class),
      @XmlElement(name = "showPanel", type = ShowPanelActionDecl.class),
   })
   private List<AbstractActionDecl> actions;

   /**
    * @return The actions.
    */
   public List<AbstractActionDecl> getActions()
   {
      if (actions == null)
         actions = new ArrayList<AbstractActionDecl>();

      return actions;
   }
}
