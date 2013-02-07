package org.selfbus.sbhome.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.service.model.base.AbstractElement;
import org.selfbus.sbhome.service.model.base.Namespaces;

/**
 * A category which is used to categorize items.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class Category extends AbstractElement
{
}
