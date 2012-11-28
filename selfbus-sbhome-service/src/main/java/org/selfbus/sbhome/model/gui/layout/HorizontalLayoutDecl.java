package org.selfbus.sbhome.model.gui.layout;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;

/**
 * A horizontal layout.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlRootElement(name = "panel")
public class HorizontalLayoutDecl extends AbstractLayoutDecl
{
}
