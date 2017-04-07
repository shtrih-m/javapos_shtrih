package com.shtrih.tinyjavapostester;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

public class LogbackConfig {

	public static void configure(String logPath)
			throws Exception {
		// reset the default context (which may already have been initialized)
		// since we want to reconfigure it
		LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
		lc.reset();

		// setup FileAppender
		PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
		encoder1.setContext(lc);
		encoder1.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
		encoder1.start();

		FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
		fileAppender.setContext(lc);
		fileAppender.setFile(logPath);
		fileAppender.setEncoder(encoder1);
		fileAppender.start();

		// setup LogcatAppender
		PatternLayoutEncoder encoder2 = new PatternLayoutEncoder();
		encoder2.setContext(lc);
		encoder2.setPattern("%logger{12} [%thread] %msg%n");
		encoder2.start();

		LogcatAppender logcatAppender = new LogcatAppender();
		logcatAppender.setContext(lc);
		logcatAppender.setEncoder(encoder2);
		logcatAppender.start();

		// add the newly created appenders to the root logger;
		// qualify Logger to disambiguate from org.slf4j.Logger
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(fileAppender);
		root.addAppender(logcatAppender);
	}
}
