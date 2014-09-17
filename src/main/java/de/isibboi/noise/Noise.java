package de.isibboi.noise;

import org.apache.commons.cli.*;

import de.isibboi.noise.function.Function;

public class Noise {
	public static void main(String[] args) throws Exception {
		Option scale = OptionBuilder.hasArg().create('s');
		Option noiseScale = OptionBuilder.hasArg().create('n');
		Option noiseFunctionName = OptionBuilder.hasArg().create('f');
		
		Options options = new Options();
		options.addOption(scale);
		options.addOption(noiseScale);
		options.addOption(noiseFunctionName);
		
		CommandLineParser parser = new GnuParser();
		
		try {
			CommandLine commandLine = parser.parse(options, args);
			
			Function noiseFunction = (Function) Class.forName("de.isibboi.noise.function."
															  + getString(noiseFunctionName, commandLine, "Sinus")
															  + "Function").newInstance();
			
			NoiseImage n = new NoiseImage(noiseFunction,
										  getInt(scale, commandLine, 1, 100, 1),
										  getInt(noiseScale, commandLine, 1, 1000, 1));
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
}
