package org.selfbus.sbhome.model.gui.generator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;


/**
 * Generates buttons by iterating over all entries of a specific group
 */
@XmlType(name = "buttons", namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class ButtonGenerator extends AbstractForeachGenerator
{
}
