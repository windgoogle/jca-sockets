/*
 * Copyright 2009 Mark Jeffrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.jcasockets.perf;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ClientOptions {
	public static final String OPTION_HELP = "h";
	public static final String OPTION_EXECUTION_SECONDS = "s";
	public static final String OPTION_NUMBER_OF_THREAD = "t";
	public static final String OPTION_PORTS = "p";
	public static final String OPTION_IP_ADDRESS = "i";
	public static final String OPTION_MILLIS_PAUSE_BETWEEN_EXECUTIONS = "P";

	public static final int DEFAULT_MILLIS_PAUSE_BETWEEN_EXECUTIONS = 0;
	public static final int DEFAULT_NUMBER_OF_THREAD = 1;
	public static final int DEFAULT_EXECUTION_SECONDS = 60;
	public static final String OPTION_MIN_MESSAGE_SIZE = "m";
	public static final String OPTION_MAX_MESSAGE_SIZE = "M";
	public static final int DEFAULT_MIN_MESSAGE_SIZE = 0;
	public static final int DEFAULT_MAX_MESSAGE_SIZE = 10000;
	public static final String DEFAULT_IP_ADDRESS = "localhost";
	public static final Integer DEFAULT_PORT = 9000;

	private String ipAddress;
	private List<Integer> ports;
	private Options options;
	private int executionSeconds;
	private boolean helpRequested;
	private int getNumberOfThreads;
	private int minimumMessageSize;
	private int maximumMessageSize;


	private int millisPause;

	public ClientOptions() {
		options   = new Options();
        
        options.addOption(OPTION_HELP, "help", false, "Displays the Help information");
        
        addOptionWithArgument( OPTION_EXECUTION_SECONDS, "seconds", "The number of seconds of execution.",
        		"seconds");
        addOptionWithArgument( OPTION_NUMBER_OF_THREAD, "threads", "Number of threads", "threads");
        
        addOptionWithArgument(OPTION_MIN_MESSAGE_SIZE, "minSize", "Minimum size of messages", "minMessage");
        addOptionWithArgument(OPTION_MAX_MESSAGE_SIZE, "maxSize", "Maximum size of messages", "maxMessage");
        
        addOptionWithArgument( OPTION_PORTS, "ports", "Ports to connect to, default [9000]", "ports");
        addOptionWithArgument( OPTION_MILLIS_PAUSE_BETWEEN_EXECUTIONS, "pause", "Milliseconds pause between requests, default ["  
        		+ DEFAULT_MILLIS_PAUSE_BETWEEN_EXECUTIONS + "]", "pause");
        addOptionWithArgument( OPTION_IP_ADDRESS, "ipaddress", "Host to connect to, default[localhost] ", "ipaddrsss");
	}

	public void parseArguments(String... args) throws ParseException {
		CommandLineParser parser = new GnuParser();
		CommandLine commandLine = parser.parse(options, args, false);
		executionSeconds = getIntegerOption(commandLine, OPTION_EXECUTION_SECONDS, DEFAULT_EXECUTION_SECONDS);
		getNumberOfThreads = getIntegerOption(commandLine, OPTION_NUMBER_OF_THREAD, DEFAULT_NUMBER_OF_THREAD);
		minimumMessageSize = getIntegerOption(commandLine, OPTION_MIN_MESSAGE_SIZE, DEFAULT_MIN_MESSAGE_SIZE);
		maximumMessageSize = getIntegerOption(commandLine, OPTION_MAX_MESSAGE_SIZE, DEFAULT_MAX_MESSAGE_SIZE);
		millisPause = getIntegerOption(commandLine, OPTION_MILLIS_PAUSE_BETWEEN_EXECUTIONS, DEFAULT_MILLIS_PAUSE_BETWEEN_EXECUTIONS);
		ports = getListOption(commandLine, OPTION_PORTS, DEFAULT_PORT);

		ipAddress = getStringValue(commandLine, OPTION_IP_ADDRESS, DEFAULT_IP_ADDRESS);

		helpRequested = getBooleanOption(commandLine, OPTION_HELP);
	}


	public void printHelp(PrintStream outputStream) {
		PrintWriter printWriter = new PrintWriter(outputStream);
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(printWriter, 80, Client.class.getName() + " [options]",
				"Socket Client Performance Test", options, 3, 1, "");
		printWriter.flush();
	}

	public int getExecutionSeconds() {
		return executionSeconds;
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

	public String getIpAddress() {
		return ipAddress;
	}

	private List<Integer> getListOption(CommandLine commandLine, String optionString, Integer defaultValue) {
		List<Integer> values = new ArrayList<Integer>();
		String stringValue = getStringValue(commandLine, optionString);

		if (stringValue != null) {
			String[] optionValues = stringValue.split(",");
			for (String optionValue : optionValues) {
				values.add(Integer.valueOf(optionValue));
			}
		}
		if (values.isEmpty()) {
			values.add(defaultValue);
		}

		return values;
	}

	private boolean getBooleanOption(CommandLine commandLine, String optionString) {
		return commandLine.hasOption(optionString);
	}

    private int getIntegerOption(CommandLine commandLine, String optionString, int defaultValue) throws ParseException {
		String optionValueString = getStringValue(commandLine, optionString);
		return optionValueString == null ? defaultValue : Integer.parseInt(optionValueString);
	}

	private String getStringValue(CommandLine commandLine, String optionString) {
		String optionValue = commandLine.getOptionValue(optionString);
		return optionValue == null ? null : optionValue.trim();
	}

	private String getStringValue(CommandLine commandLine, String optionString, String defaultValue) {
		String optionValueString = getStringValue(commandLine, optionString);
		return optionValueString == null ? defaultValue : optionValueString;
	}


	private void addOptionWithArgument( String shortOptionName, String longOptionName,
			String optionDescription, String optionArgument) {
		Option option = new Option(shortOptionName, longOptionName, true, optionDescription);
		option.setArgName(optionArgument);
		options.addOption(option);
	}

	public int getMillisPause() {
		return millisPause;
	}
	
}