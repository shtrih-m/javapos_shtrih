package com.shtrih.tinyjavapostester;

import java.io.File;

import org.apache.log4j.Level;

import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Call {@link #configure()} from your application's activity.
 */

public class ConfigureLog4J {
	public static void configure() {
		final LogConfigurator logConfigurator = new LogConfigurator();

		logConfigurator.setUseLogCatAppender(true);
		logConfigurator.setUseFileAppender(false);
		logConfigurator.setFileName(Environment.getExternalStorageDirectory()
				+ File.separator + "myapp.log");
		logConfigurator.setRootLevel(Level.DEBUG);
		// Set log level of a specific logger
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.configure();
	}
}