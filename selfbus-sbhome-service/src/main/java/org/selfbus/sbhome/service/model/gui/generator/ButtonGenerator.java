package org.selfbus.sbhome.service.model.gui.generator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.base.Namespaces;


/**
 * Generates buttons by iterating over all entries of a specific group
 */
@XmlType(name = "buttons", namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class ButtonGenerator extends AbstractForeachGenerator
{
}
