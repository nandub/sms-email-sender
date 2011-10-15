package net.hellosaint.lib.smsemail;

import static net.hellosaint.lib.smsemail.SmsOrEmail.EOL;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class SmsGateways {
	private static SortedMap<String, String> sms = new TreeMap<String, String>();
	static {
		sms.put("alltel", "@message.alltel.com");
		sms.put("att", "@txt.att.net");
		sms.put("boost", "@myboostmobile.com");
		sms.put("metrocall", "@page.metrocall.com");
		sms.put("metropcs", "@mymetropcs.com");
		sms.put("nextel", "@messaging.nextel.com");
		sms.put("sprint", "@messaging.sprintpcs.com");
		sms.put("tmobile", "@tmomail.net");
		sms.put("virgin", "@vmobl.com");
		sms.put("verizon", "@vtext.com");
	}

	public static String getGateway(Object carrier) {
		return sms.get(carrier);
	}

	public static String getAvailableCarriers() {
		StringBuffer buf = new StringBuffer();
		buf.append("Available Carriers:" + EOL);
		Iterator<String> carriers = sms.keySet().iterator();
		while (carriers.hasNext()) {
			buf.append(EOL + carriers.next());
		}
		return buf.toString();
	}
}
