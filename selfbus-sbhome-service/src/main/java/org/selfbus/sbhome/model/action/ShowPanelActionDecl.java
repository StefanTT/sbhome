package org.selfbus.sbhome.model.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.base.Namespaces;

/**
 * Show a panel in the GUI.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public class ShowPanelActionDecl extends AbstractActionDecl
{
   @XmlAttribute(name = "panel")
   protected String panel;

   /**
    * @return the ID of the panel
    */
   public String getPanel()
   {
      return panel;
   }

   /**
    * @param panel - the ID of the panel
    */
   public void setPanel(String panel)
   {
      this.panel = panel;
   }
}
