package example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.selfbus.sbhome.designer.components.Accordion;

/**
 * An example for the {@link Accordion} class.
 */
public class AccordionExample
{
   public static JPanel getDummyPanel(String name)
   {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new JLabel(name, JLabel.CENTER));
      return panel;
   }

   public static void main(String[] args)
   {
      JFrame frame = new JFrame("Accordion Test");
      Accordion outlookBar = new Accordion();
      outlookBar.addBar("One", getDummyPanel("One"));
      outlookBar.addBar("Two", getDummyPanel("Two"));
      outlookBar.addBar("Three", getDummyPanel("Three"));
      outlookBar.addBar("Four", getDummyPanel("Four"));
      outlookBar.addBar("Five", getDummyPanel("Five"));
      outlookBar.setVisibleBar(2);
      frame.getContentPane().add(outlookBar);

      frame.setSize(800, 600);
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      frame.setLocation(d.width / 2 - 400, d.height / 2 - 300);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
}
