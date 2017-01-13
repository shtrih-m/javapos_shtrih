package com.shtrih.tinyjavapostester;

import java.io.File;

import org.apache.log4j.Level;

import android.content.Context;
import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class ConfigureLog4J {
	public static void configure(Context context) {
		final LogConfigurator logConfigurator = new LogConfigurator();

		logConfigurator.setUseLogCatAppender(true);
		logConfigurator.setUseFileAppender(false);
		logConfigurator.setFileName(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "tinyJavaPOSTester.log");
		logConfigurator.setRootLevel(Level.DEBUG);
		// Set log level of a specific logger
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.configure();
	}
}