package org.selfbus.sbhome.model.program;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.selfbus.sbhome.model.Namespaces;

/**
 * A connector connects program variables to group values and acts as output variable.
 */
@XmlType(namespace = Namespaces.PROJECT)
@XmlAccessorType(XmlAccessType.FIELD)
public class ProgramOutputConnector extends AbstractProgramConnector
{
}
