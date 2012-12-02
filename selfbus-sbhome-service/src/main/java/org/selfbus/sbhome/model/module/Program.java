package org.selfbus.sbhome.model.module;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.jexl2.Expression;
import org.selfbus.sbhome.base.Namespaces;

/**
 * A small script. 
 */
@XmlType(namespace = Namespaces.PROJECT, propOrder = {})
@XmlAccessorType(XmlAccessType.NONE)
public class Program
{
   @XmlAttribute
   private String id;

   @XmlElementWrapper(name = "connectors")
   @XmlElements
   ({
      @XmlElement(name = "input", type = ProgramInputConnector.class),
      @XmlElement(name = "output", type = ProgramOutputConnector.class)
   })
   private Set<AbstractProgramConnector> connectors;

   @XmlElement(name = "code", required = true)
   private String code;

   @XmlTransient
   private Expression expression;

   /**
    * @return The ID of the program.
    */
   public String getId()
   {
      return id;
   }

   /**
    * Set the ID of the program.
    *
    * @param id the id to set
    */
   public void setId(String id)
   {
      this.id = id;
   }

   /**
    * @return The connectors
    */
   public Set<AbstractProgramConnector> getConnectors()
   {
      return connectors;
   }

   /**
    * Set the connectors.
    * @param connectors - the connectors
    */
   public void setConnectors(Set<AbstractProgramConnector> connectors)
   {
      this.connectors = connectors;
   }

   /**
    * @return the program code
    */
   public String getCode()
   {
      return code;
   }

   /**
    * Set the program code.
    *
    * @param code - the code to set
    */
   public void setCode(String code)
   {
      this.code = code;
   }

   /**
    * @return the expression
    */
   public Expression getExpression()
   {
      return expression;
   }

   /**
    * @param expression the expression to set
    */
   public void setExpression(Expression expression)
   {
      this.expression = expression;
   }
}
