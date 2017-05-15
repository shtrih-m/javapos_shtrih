/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.model;

/**
 *
 * @author V.Kravtsov
 */
import java.io.InputStream;
import java.io.Serializable;
import java.util.Vector;

import com.shtrih.util.CompositeLogger;

import com.shtrih.util.ResourceLoader;
import com.shtrih.util.SysUtils;

public class PrinterModels implements Serializable {

	private Vector list = new Vector();
	private static CompositeLogger logger = CompositeLogger.getLogger(PrinterModels.class);

	/** Creates a new instance of PrinterModels */
	public PrinterModels() {
	}

	public void clear() {
		list.clear();
	}

	public int size() {
		return list.size();
	}

	public void add(PrinterModel item) {
		list.add(item);
	}

	public PrinterModel get(int index) {
		return (PrinterModel) list.get(index);
	}

	public Vector getList() {
		return list;
	}

	public void setList(Vector list) {
		this.list = list;
	}

	public void setDefaults() {
		try {
			clear();

			add(new PrinterModelDefault());
			add(new PrinterModelShtrih950K());
			add(new PrinterModelShtrih950K2());
			add(new PrinterModelShtrihComboFRK());
			add(new PrinterModelShtrihComboFRK2());
			add(new PrinterModelShtrihFRFv3());
			add(new PrinterModelShtrihFRFv4());
			add(new PrinterModelShtrihFRK());
			add(new PrinterModelShtrihKioskFRK());
			add(new PrinterModelShtrihLightFRK());
			add(new PrinterModelShtrihLightFRK2());
			add(new PrinterModelShtrihMFRK());
			add(new PrinterModelShtrihMPTK());
			add(new PrinterModelShtrihMiniFRK());
			add(new PrinterModelShtrihMiniFRK2());
			add(new PrinterModelYarus01K());
			add(new PrinterModelYarus01K2());
			add(new PrinterModelYarus02K());
			add(new PrinterModelASPDElves());
			add(new PrinterModelShtrihFRFBelarus());
			add(new PrinterModelShtrihMobileF());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	// load from resources
	public void load() {
		try {
			InputStream stream = ResourceLoader.load("models.xml");
			XmlModelsReader reader = new XmlModelsReader(this);
			reader.load(stream);

		} catch (Exception e) {
			logger.error(e);
			setDefaults();
		}
	}

	public void save(String fileName) {
		try {
			XmlModelsWriter writer = new XmlModelsWriter(this);
			writer.save(SysUtils.getFilesPath() + fileName);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public PrinterModel itemByID(int id) {
		for (int i = 0; i < size(); i++) {
			PrinterModel result = get(i);
			if (result.getId() == id) {
				return result;
			}
		}
		return null;
	}

	public PrinterModel itemByModel(int model) {
		for (int i = 0; i < size(); i++) {
			PrinterModel result = get(i);
			if (result.getModelID() == model) {
				return result;
			}
		}
		return null;
	}

	public PrinterModels itemsByModel(int modelID) throws Exception {
		PrinterModels models = new PrinterModels();
		for (int i = 0; i < size(); i++) {
			PrinterModel model = get(i);
			if (model.getModelID() == modelID) {
				models.add(model);
			}
		}
		return models;
	}

	public PrinterModel find(int modelID, int protocolVersion,
			int protocolSubVersion) throws Exception {
		int distance = 0xFFFF;
		PrinterModel result = null;
		for (int i = 0; i < size(); i++) {
			PrinterModel model = get(i);
			if (model.getModelID() == modelID) {
				int d = Math.abs((model.getProtocolVersion() * 256 + model
						.getProtocolSubVersion())
						- (protocolVersion * 256 + protocolSubVersion));
				if (d < distance) {
					distance = d;
					result = model;
				}
			}
		}
		return result;
	}
}
