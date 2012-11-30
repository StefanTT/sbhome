package org.selfbus.sbhome.web.panels;

import java.io.IOException;

import org.freebus.knxcomm.BusInterface;
import org.freebus.knxcomm.BusInterfaceFactory;
import org.freebus.knxcomm.link.netip.KNXnetLink;
import org.freebus.knxcomm.link.serial.SerialPortUtil;
import org.freebus.knxcomm.types.LinkMode;
import org.freebus.knxcomm.types.LinkType;
import org.selfbus.sbhome.service.Daemon;
import org.selfbus.sbhome.web.SbHomeApplication;
import org.selfbus.sbhome.web.misc.I18n;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * The settings panel, for configuration.
 */
public class SettingsPanel extends Panel
{
   private static final long serialVersionUID = -8865864746729204629L;

   private final SbHomeApplication application;
   private final AbstractLayout bodyLayout;
   private final ComboBox themeComboBox = new ComboBox();
   private final ComboBox linkTypeComboBox = new ComboBox();
   private AbstractComponent linkDetails;

   private final ObjectProperty<String> knxnetHost = new ObjectProperty<String>("localhost");
   private final ObjectProperty<String> knxnetPort = new ObjectProperty<String>("" + KNXnetLink.defaultPortUDP);

   /**
    * Create a settings panel.
    * 
    * @param application - the application
    */
   public SettingsPanel(final SbHomeApplication application)
   {
      super(I18n.getMessage("SettingsPanel.title"));
      this.application = application;

      bodyLayout = new VerticalLayout();
      setContent(bodyLayout);

      setupThemeSelection();
      setupBusInterface();
   }

   /**
    * Setup the theme selection.
    */
   protected void setupThemeSelection()
   {
      Label caption = new Label(I18n.getMessage("SettingsPanel.theme"));
      //caption.addStyleName("paragraph");
      bodyLayout.addComponent(caption);

      bodyLayout.addComponent(themeComboBox);

      themeComboBox.addItem("sbhome-black");
      themeComboBox.setItemCaption("sbhome-black", I18n.getMessage("SettingsPanel.themeBlack"));

      themeComboBox.addItem("sbhome-blue");
      themeComboBox.setItemCaption("sbhome-blue", I18n.getMessage("SettingsPanel.themeBlue"));

      themeComboBox.addItem("sbhome-green");
      themeComboBox.setItemCaption("sbhome-green", I18n.getMessage("SettingsPanel.themeGreen"));

      themeComboBox.select(application.getTheme());
      themeComboBox.setTextInputAllowed(false);
      themeComboBox.setNullSelectionAllowed(false);

      themeComboBox.addListener(new ValueChangeListener()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void valueChange(ValueChangeEvent event)
         {
            String theme = (String) themeComboBox.getValue();
            application.setTheme(theme);
         }
      });
   }

   /**
    * Setup the bus interface.
    */
   protected void setupBusInterface()
   {
      Label caption = new Label(I18n.getMessage("SettingsPanel.busInterface"));
      caption.addStyleName("paragraph");
      bodyLayout.addComponent(caption);

      bodyLayout.addComponent(linkTypeComboBox);

      linkTypeComboBox.select(application.getTheme());
      linkTypeComboBox.setTextInputAllowed(false);
      linkTypeComboBox.setNullSelectionAllowed(false);

      for (LinkType linkType : LinkType.values())
      {
         linkTypeComboBox.addItem(linkType);
         linkTypeComboBox.setItemCaption(linkType, linkType.label);
      }

      linkDetails = new Label();
      bodyLayout.addComponent(linkDetails);

      linkTypeComboBox.addListener(new ValueChangeListener()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void valueChange(ValueChangeEvent event)
         {
            LinkType linkType = (LinkType) linkTypeComboBox.getValue();
            AbstractComponent newLinkDetails = null;

            if (LinkType.KNXNET_IP.equals(linkType))
            {
               newLinkDetails = createKNXnetIPDetails();
            }
            else if (LinkType.SERIAL_FT12.equals(linkType))
            {
               newLinkDetails = createSerialDetails();
            }
            else
            {
               newLinkDetails = new Label();
            }

            bodyLayout.replaceComponent(linkDetails, newLinkDetails);
            linkDetails = newLinkDetails;
         }
      });

      linkTypeComboBox.select(LinkType.NONE);
   }

   /**
    * Create a component for editing details for the KNXnet/IP link type
    * 
    * @return The created component.
    */
   protected AbstractComponent createKNXnetIPDetails()
   {
      AbstractLayout mainLayout = new VerticalLayout();

      TextField hostField = new TextField(I18n.getMessage("SettingsPanel.knxnetHost"), knxnetHost);
      hostField.addStyleName("paragraph");
      mainLayout.addComponent(hostField);

      TextField portField = new TextField(I18n.getMessage("SettingsPanel.knxnetPort"), knxnetPort);
      portField.addStyleName("paragraph");
      portField.setInvalidCommitted(false);
      mainLayout.addComponent(portField);

      final Button connectButton = createConnectButton();
      mainLayout.addComponent(connectButton);
      connectButton.addListener(new ClickListener()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void buttonClick(ClickEvent event)
         {
            String host = (String) knxnetHost.getValue();
            int port = Integer.parseInt(knxnetPort.getValue());
            BusInterface iface = BusInterfaceFactory.newKNXnetInterface(host, port);
            try
            {
               iface.open(LinkMode.BusMonitor);
               Daemon.getInstance().setBusInterface(iface);
            }
            catch (IOException e)
            {
               connectButton.setEnabled(true);

               application.getMainWindow().showNotification(I18n.getMessage("SettingsPanel.connectFailed"),
                  e.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
            }
         }
      });

      return mainLayout;
   }

   /**
    * Create a component for editing details for the serial link type
    * 
    * @return The created component.
    */
   protected AbstractComponent createSerialDetails()
   {
      AbstractLayout mainLayout = new VerticalLayout();

      final ComboBox portComboBox = new ComboBox(I18n.getMessage("SettingsPanel.serialPort"));
      mainLayout.addComponent(portComboBox);
      portComboBox.setTextInputAllowed(false);
      portComboBox.setNullSelectionAllowed(false);

      for (String portName : SerialPortUtil.getPortNames())
         portComboBox.addItem(portName);

      final Button connectButton = createConnectButton();
      mainLayout.addComponent(connectButton);

      portComboBox.addListener(new ValueChangeListener()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void valueChange(ValueChangeEvent event)
         {
            connectButton.setEnabled(true);
         }
      });

      connectButton.setEnabled(false);
      connectButton.addListener(new ClickListener()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public void buttonClick(ClickEvent event)
         {
            String portName = (String) portComboBox.getValue();
            BusInterface iface = BusInterfaceFactory.newSerialInterface(portName);
            try
            {
               iface.open(LinkMode.BusMonitor);
               Daemon.getInstance().setBusInterface(iface);
            }
            catch (IOException e)
            {
               connectButton.setEnabled(true);

               application.getMainWindow().showNotification(I18n.getMessage("SettingsPanel.connectFailed"),
                  e.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
            }
         }
      });

      return mainLayout;
   }

   /**
    * Create a connect button.
    * 
    * @return The created button.
    */
   protected Button createConnectButton()
   {
      Button btn = new Button(I18n.getMessage("SettingsPanel.connect"));
      btn.addStyleName("paragraph");
      btn.addStyleName("v-button-default");
      btn.setDisableOnClick(true);

      return btn;
   }
}
