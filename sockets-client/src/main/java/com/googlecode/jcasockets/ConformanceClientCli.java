package com.googlecode.jcasockets;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ConformanceClientCli {
	public static final String OPTION_HELP = "h";
	public static final String OPTION_EXECUTION_SECONDS = "s";
	public static final String OPTION_NUMBER_OF_THREAD = "t";
	public static final String OPTION_PORTS = "p";

	public static final int DEFAULT_NUMBER_OF_THREAD = 1;
	public static final int DEFAULT_EXECUTION_SECONDS = 60;
	public static final String OPTION_MIN_MESSAGE_SIZE = "m";
	public static final String OPTION_MAX_MESSAGE_SIZE = "M";
	public static final int DEFAULT_MIN_MESSAGE_SIZE = 0;
	public static final int DEFAULT_MAX_MESSAGE_SIZE = 10000;
	
	private Options options;
	private int executionSeconds;
	private boolean helpRequested;
	private int getNumberOfThreads;
	private List<Integer> ports;
	private int minimumMessageSize;
	private int maximumMessageSize;

	public ConformanceClientCli() {
		options = createOptions();
	}

	public void parseArguments(String... args) throws ParseException {
		CommandLineParser parser = new GnuParser();
		CommandLine commandLine = parser.parse(options, args, false);
		executionSeconds = getIntegerOption(commandLine,
				OPTION_EXECUTION_SECONDS, DEFAULT_EXECUTION_SECONDS);
		getNumberOfThreads = getIntegerOption(commandLine,
				OPTION_NUMBER_OF_THREAD, DEFAULT_NUMBER_OF_THREAD);
		minimumMessageSize = getIntegerOption(commandLine,
				OPTION_MIN_MESSAGE_SIZE, DEFAULT_MIN_MESSAGE_SIZE);
		maximumMessageSize = getIntegerOption(commandLine,
				OPTION_MAX_MESSAGE_SIZE, DEFAULT_MAX_MESSAGE_SIZE);
		ports = getListOption(commandLine, OPTION_PORTS);
		helpRequested = getBooleanOption(commandLine, OPTION_HELP);
	}

	private List<Integer> getListOption(CommandLine commandLine, String optionString) {
		List<Integer> values = new ArrayList<Integer>();
		String stringValue = getStringValue(commandLine, optionString);
		
		if (stringValue != null) {
			String[] optionValues = stringValue.split(",");
			for (String optionValue : optionValues) {
				values.add(Integer.valueOf(optionValue));
			}
		}
		return values;
	}

	private boolean getBooleanOption(CommandLine commandLine,
			String optionString) {
		return commandLine.hasOption(optionString);
	}

	private int getIntegerOption(CommandLine commandLine, String optionString,
			int defaultValue) throws ParseException {
		String optionValueString = getStringValue(commandLine, optionString);
		return optionValueString == null ? defaultValue : Integer
				.parseInt(optionValueString);
	}

	private String getStringValue(CommandLine commandLine, String optionString) {
		String optionValue = commandLine.getOptionValue(optionString);
		return optionValue == null ? null : optionValue.trim();
	}

	public int getExecutionSeconds() {
		return executionSeconds;
	}

	private Options createOptions() {
		Options options = new Options();

		options.addOption(OPTION_HELP, "help", false,
				"Displays the Help information");

		addOptionWithArgument(options, OPTION_EXECUTION_SECONDS, "seconds",
				"The number of seconds of execution.", "seconds");
		addOptionWithArgument(options, OPTION_NUMBER_OF_THREAD, "threads",
				"Number of threads", "threads");

		addOptionWithArgument(options, OPTION_MIN_MESSAGE_SIZE, "minSize",
				"Minimum size of messages", "minMessage");
		addOptionWithArgument(options, OPTION_MAX_MESSAGE_SIZE, "maxSize",
				"Maximum size of messages", "maxMessage");

		addOptionWithArgument(options, OPTION_PORTS, "ports",
				"Ports to connect to", "ports");

		return options;
	}

	private void addOptionWithArgument(Options options, String shortOptionName,
			String optionName, String optionDescription, String optionArgument) {
		Option option = new Option(shortOptionName, optionName, true,
				optionDescription);
		option.setArgName(optionArgument);
		options.addOption(option);
	}

	public boolean isHelpRequested() {
		return helpRequested;
	}

	public int getNumberOfThreads() {
		return getNumberOfThreads;
	}

	public List<Integer> getPorts() {
		return ports;
	}

	public int getMinimumMessageSize() {
		return minimumMessageSize;
	}
	public int getMaximumMessageSize() {
		return maximumMessageSize;
	}
}
