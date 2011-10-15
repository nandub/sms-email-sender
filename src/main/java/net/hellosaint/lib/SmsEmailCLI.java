package net.hellosaint.lib;

import java.io.PrintWriter;

import net.hellosaint.lib.smsemail.SmsGateways;
import net.hellosaint.lib.smsemail.SmsOrEmail;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class SmsEmailCLI {
	public static final String EOL = System.getProperty("line.separator");
	public static final String FOOTER = "For contact information, type --info";
	public static final String HEADER = "Command line for sms-send, Copyright 2011, Fernando Ortiz";
	public static final Options options;
	public static final CommandLineParser parser = new PosixParser();
	
	static {
		options = new Options();
		options.addOption("d", "debug", false, "enable debugging.");
		options.addOption("i", "info", false, "contact information.");
		options.addOption("h", "help", false, "print this message.");
		options.addOption("f", "from", true,
				"set the user sending the sms/email message.");
		options.addOption(
				"t",
				"to",
				true,
				"send to a list of users.  to, should be a comma separated list of names.  (format: sms-number/email-name@sms-carrier/domain-email)");
		options.addOption(
				"b",
				"bcc",
				true,
				"send blind carbon copies to a list of users.  bcc, should be a comma separated list of names. (format: sms-number/email-name@sms-carrier/domain-email)");
		options.addOption(
				"c",
				"cc",
				true,
				"send carbon copies to a list of users.  cc, should be a comma separated list of names. (format: sms-number/email-name@sms-carrier/domain-email)");
		options.addOption("s", "subject", true,
				"subject to send; be careful to quote subjects containing spaces.");
		options.addOption("m", "message", true,
				"the text message to send; be careful to quote the message containing spaces.");
		options.addOption("H", "host", true,
				"host, the email host server (default: mailhost).");
		options.addOption("p", "port", true,
				"port, the email host server (default: 25).");
		options.addOption("l", "sms-carriers", false,
				"list valid SMS carriers.");
	}
	

	// Main function executed
	public static void main(String[] args) throws Exception {
		SmsEmailCLI cli = new SmsEmailCLI();
		HelpFormatter help = new HelpFormatter();
		PrintWriter pw = new PrintWriter(System.out);
		int width = HelpFormatter.DEFAULT_WIDTH;
		if (args.length == 0) {
			cli.showUsage(pw, width, help);
			System.exit(1);
		}
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
		} catch (ParseException ex) {
			cli.showUsage(pw, width, help);
			ex.printStackTrace();
			System.exit(1);
		}
		
		SmsOrEmail sender = new SmsOrEmail();
		sender.debugging = line.hasOption('d'); // debugging
		if (line.hasOption('i')) {
			pw.append("fernando.ortiz@navy.mil (Fernando Ortiz)");
			pw.flush();
			System.exit(0);
		}
		if (line.hasOption('h')) {
			help.printHelp(pw, width, "sms-send", HEADER, options, 4, 2,
					FOOTER, true);
			pw.flush();
			System.exit(0);
		}
		if (line.hasOption('l')) {
			String list = SmsGateways.getAvailableCarriers();
			System.out.println(list);
			System.exit(0);
		}
		if (line.hasOption('H')) {
			sender.setHost(line.getOptionValue('H'));
		}
		if (line.hasOption('p')) {
			sender.setPort(Integer.parseInt(line.getOptionValue('p')));
		}
		if (line.hasOption('f')) {
			sender.setFrom(line.getOptionValue('f'));
		}
		if (line.hasOption('t')) {
			sender.setTo(line.getOptionValue('t'));
		}
		if (line.hasOption('b')) {
			sender.setBCC(line.getOptionValue('b'));
		}
		if (line.hasOption('c')) {
			sender.setCC(line.getOptionValue('c'));
		}
		if (line.hasOption('s')) {
			sender.setSubject(line.getOptionValue('s'));
		}
		if (line.hasOption('m')) {
			sender.setMessage(line.getOptionValue('m'));
		}
		boolean ret = sender.send();
		System.out.println(EOL);
		if (ret) {
			System.out.println("Message was sent!");
		} else {
			System.out.println("Message was not sent - "
					+ sender.getLastError());
		}
	}
	
	private void showUsage(PrintWriter pw, int width, HelpFormatter help) {
		help.printUsage(pw, width, "Try 'sms-send --help' for more options.");
		pw.flush();
	}
}
