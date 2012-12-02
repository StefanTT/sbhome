package org.selfbus.sbhome.model.module;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.base.AbstractNamed;
import org.selfbus.sbhome.base.Namespaces;

/**
 * A module
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class Module extends AbstractNamed
{
   @XmlAttribute(name = "type", required = true)
   private String moduleTypeName;

   private ModuleType moduleType;
}
