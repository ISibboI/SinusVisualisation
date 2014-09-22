package de.isibboi.noise;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.isibboi.noise.function.Function;

public class Noise {
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		Option scale = OptionBuilder.hasArg().withDescription("The size of the image.").create('s');
		Option noiseScale = OptionBuilder.hasArg().withDescription("The size of the pattern.").create('n');
		Option noiseFunctionName = OptionBuilder.hasArg().withDescription("Available functions can be retrieved with -l.").create('f');
		Option help = OptionBuilder.withLongOpt("help").withDescription("Prints this message.").create('h');
		Option list = OptionBuilder.hasArg().withDescription("Lists most available functions. There might be more functions on the classpath in non-default locations, those won't be listed.").create('l');
		
		Options options = new Options();
		options.addOption(scale);
		options.addOption(noiseScale);
		options.addOption(noiseFunctionName);
		options.addOption(help);
		options.addOption(list);
		
		CommandLineParser parser = new GnuParser();
		
		try {
			CommandLine commandLine = parser.parse(options, args);
			
			if (hasOption(help, commandLine)) {
				new HelpFormatter().printHelp("Noise", options);
				return;
			}
			
			if (hasOption(list, commandLine)) {
				listFunctions();
				return;
			}
			
			Function noiseFunction = (Function) Class.forName("de.isibboi.noise.function."
															  + getString(noiseFunctionName, commandLine, "Sinus")
															  + "Function").newInstance();
			
			NoiseImage n = new NoiseImage(noiseFunction,
										  getInt(scale, commandLine, 1, 100, 1),
										  getInt(noiseScale, commandLine, 1, 1000, 1));
			n.save();
		} catch (ParseException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public static boolean hasOption(Option o, CommandLine commandLine) {
		return commandLine.hasOption(o.getOpt());
	}
	
	public static String getString(Option o, CommandLine commandLine) throws ParseException {
		String value = commandLine.getOptionValue(o.getOpt());
		
		if (value == null) {
			throw new ParseException("Option " + o.getOpt() + " is missing.");
		} else {
			return value;
		}
	}
	
	public static String getString(Option o, CommandLine commandLine, String defaultValue) throws ParseException {
		if (hasOption(o, commandLine)) {
			return getString(o, commandLine);
		} else {
			return defaultValue;
		}
	}
	
	public static int getInt(Option o, CommandLine commandLine) throws ParseException {
		try {
			return Integer.parseInt(getString(o, commandLine));
		} catch (NumberFormatException e) {
			throw new ParseException("Option " + o.getOpt() + " has to be an integer. (Is: "
									 + commandLine.getOptionValue(o.getOpt()) + ")");
		}
	}
	
	public static int getInt(Option o, CommandLine commandLine, int min, int max) throws ParseException {
		int value = getInt(o, commandLine);
		
		if (value < min) {
			throw new ParseException("Option " + o.getOpt() + " is too small: " + value + " < " + min);
		}
		
		if (value > max) {
			throw new ParseException("Option " + o.getOpt() + " is too large: " + value + " > " + max);
		}
		
		return value;
	}
	
	public static int getInt(Option o, CommandLine commandLine, int min, int max, int defaultValue) throws ParseException {
		if (hasOption(o, commandLine)) {
			return getInt(o, commandLine, min, max);
		} else {
			return defaultValue;
		}
	}
	
	public static void listFunctions() {
		System.out.println("To be implemented");
	}
}
