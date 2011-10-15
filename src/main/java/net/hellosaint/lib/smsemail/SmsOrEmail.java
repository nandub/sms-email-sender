package net.hellosaint.lib.smsemail;

//Package Imports
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * Original code taken from http://www.wirelessdevnet.com/channels/java/features/simplewire/
 * 
 */
public class SmsOrEmail {

	public static final String EOL = System.getProperty("line.separator");	

	// Global Variables
	private InternetAddress[] TO;
	private InternetAddress[] BCC;
	private InternetAddress[] CC;
	private InternetAddress FROM;
	private String LASTERROR;
	private String MAILHOST;
	private int PORT;
	private String SUBJECT;
	private String MESSAGE;
	public boolean debugging = false;	

	// Public Constructor
	public SmsOrEmail() {
		this.TO = null;
		this.FROM = null;
		this.SUBJECT = null;
		this.MESSAGE = null;
		this.MAILHOST = "mailhost";
		this.PORT = 25;
		this.LASTERROR = "No method called.";
	}

	// Public Constructor
	public SmsOrEmail(String from, String mailhost, int mailport) throws AddressException {
		this.setFrom(from);
		this.setHost(mailhost);
		this.setPort(mailport);
	}

	public InternetAddress getFrom() {
		return FROM;
	}

	public InternetAddress[] getTo() {
		return TO;
	}

	public InternetAddress[] getBCC() {
		return BCC;
	}

	public InternetAddress[] getCC() {
		return CC;
	}

	public String getLastError() {
		return LASTERROR;
	}

	public String getHost() {
		return MAILHOST;
	}

	public int getPort() {
		return PORT;
	}

	public String getSubject() {
		return SUBJECT;
	}

	public String getMessage() {
		return MESSAGE;
	}

	// Will attempt to send the Email SMS and return a boolean meaning it
	// either failed or succeeded.
	public boolean send() {

		// Set Email Properties
		Properties props = new Properties();

		props.put("mail.debug", debugging);

		if (MAILHOST != null) {
			props.put("mail.smtp.host", MAILHOST);
		}
		if (PORT != 25) {
			props.put("mail.smtp.port", PORT);
		}
		if (FROM != null) {
			props.put("mail.from", FROM);
		}

		// Get a Session object
		Session session = Session.getDefaultInstance(props);

		try {

			// Construct the email
			Message msg = new MimeMessage(session);

			// Set From
			if (FROM != null) {
				msg.setFrom(FROM);
			} else {
				msg.setFrom();
			}

			// Add Recipient
			if (TO != null && TO.length > 0) {
				msg.addRecipients(Message.RecipientType.TO, TO);
			}
			if (BCC != null && BCC.length > 0) {
				msg.addRecipients(Message.RecipientType.BCC, BCC);
			}
			if (CC != null && CC.length > 0) {
				msg.addRecipients(Message.RecipientType.CC, CC);
			}

			// Set Subject
			if (SUBJECT != null) {
				msg.setSubject(SUBJECT);
			}

			// Set Text
			if (MESSAGE != null) {
				msg.setText(MESSAGE);
			}

			// Sent Date
			msg.setSentDate(new Date());
			if (debugging) {
				try {
					msg.writeTo(System.out);
				} catch (IOException e) {
				}
			}

			// Send Message
			Transport.send(msg);

			LASTERROR = "Success.";
			return true;
		} catch (MessagingException mex) {
			LASTERROR = mex.getMessage();
			return false;
		}
	}

	public void setFrom(String from) throws AddressException {
		this.setFrom(parseRecipients(from)[0]);
	}

	public void setFrom(InternetAddress from) {
		this.FROM = from;
	}

	public void setTo(String to) throws AddressException {
		this.setTo(parseRecipients(to));
	}

	public void setTo(InternetAddress[] to) {
		this.TO = to;
	}

	public void setBCC(String bcc) throws AddressException {
		this.setBCC(parseRecipients(bcc));
	}

	public void setBCC(InternetAddress[] bcc) {
		this.BCC = bcc;
	}

	public void setCC(String cc) throws AddressException {
		this.setCC(parseRecipients(cc));
	}

	public void setCC(InternetAddress[] cc) {
		this.CC = cc;
	}

	public void setHost(String host) {
		this.MAILHOST = host;
	}

	public void setPort(int port) {
		this.PORT = port;
	}

	public void setSubject(String subject) {
		this.SUBJECT = subject;
	}

	public void setMessage(String message) {
		this.MESSAGE = message;
	}

	

	private static InternetAddress[] parseRecipients(String addr)
			throws AddressException {
		String[] addrs = addr.split(",");
		InternetAddress[] addresses = new InternetAddress[addrs.length];
		for (int i = 0; i < addrs.length; i++) {
			String address = addrs[i];
			String smsNumberEmail = address.substring(0, address.indexOf("@"));
			String carrierDomainName = address
					.substring(address.indexOf("@") + 1);
			String gateway = SmsGateways.getGateway(carrierDomainName);
			if (gateway != null) {
				address = smsNumberEmail + gateway;
			} else {
				address = smsNumberEmail + "@" + carrierDomainName;
			}
			addresses[i] = new InternetAddress(address);
		}
		return addresses;
	}
}
