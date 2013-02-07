package org.selfbus.sbhome.service.model.gui.layout;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * A horizontal layout.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlRootElement(name = "panel")
public class HorizontalLayoutDecl extends AbstractLayoutDecl
{
}
