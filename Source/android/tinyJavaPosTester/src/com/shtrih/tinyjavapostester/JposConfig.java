package com.shtrih.tinyjavapostester;

import android.content.Context;

import com.shtrih.util.SysUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jpos.config.JposEntry;
import jpos.config.JposEntryRegistry;
import jpos.loader.JposServiceLoader;
import jpos.util.JposPropertiesConst;

public class JposConfig {

	public static void configure(String deviceName, String portName, Context context)
			throws Exception {
		copyAsset("jpos.xml", SysUtils.getFilesPath() + "jpos.xml", context);
		String fileURL = "file://" + SysUtils.getFilesPath() + "jpos.xml";
		System.setProperty(
				JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME, fileURL);

		System.setProperty(
				JposPropertiesConst.JPOS_REG_POPULATOR_CLASS_PROP_NAME,
				"jpos.config.simple.xml.SimpleXmlRegPopulator");

		JposEntryRegistry registry = JposServiceLoader.getManager()
				.getEntryRegistry();
		if (registry.hasJposEntry(deviceName)) {
			JposEntry jposEntry = registry.getJposEntry(deviceName);
			if (jposEntry != null) {
				jposEntry.addProperty("portName", portName);
				jposEntry.modifyPropertyValue("portName", portName);
			}
		}
	}

	public static void copyAsset(String assetFileName, String destFile, Context context)
			throws Exception {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = context.getAssets().open(assetFileName);
			os = new FileOutputStream(new File(destFile));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}

	}
}
