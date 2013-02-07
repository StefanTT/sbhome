package org.selfbus.sbhome.service.model.trigger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * A trigger that reacts on mouse clicks.
 */
@XmlRootElement(name = "trigger")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = Namespaces.PROJECT)
public class ClickTriggerDecl extends AbstractTriggerDecl
{
}
