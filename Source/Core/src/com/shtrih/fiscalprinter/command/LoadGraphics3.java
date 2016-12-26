/*
 * LoadExtendedGraphics.java
 *
 * Created on April 2 2008, 21:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 * Загрузка расширенной графики 512 Команда: 4EH. Длина сообщения: 11+X2 байт.
 * Пароль оператора (4 байта) Длина линии (1 байт) L = 1…64 Номер начальной
 * линии (2 байта) 1…600 Количество последующих линий3 (2 байта) N = 1…600 Тип
 * графического буфера (1 байт) 0 - для команд стандартной или расширенной
 * графики (320), 1 - для команд расширенной графики 512 Графическая информация
 * (X2 = N*L байт) Ответ: 4EH. Длина сообщения: 3 байта. Код ошибки (1 байт)
 * Порядковый номер оператора (1 байт) 1…30
 * 
 * Примечания: 1 – в зависимости от модели ККТ (для параметра модели Бит 42, см.
 * команду F7H); 2 – максимальный размер графических данных (блок) зависит от
 * длины сообщения (см. поле "Максимальная длина команды (N/LEN16)" в команде
 * F7H); 3 – при L = 64 для максимальной длины сообщения 255 максимальный размер
 * блока графики равен 3 линиям; для максимальной длины сообщения 1455
 * максимальный размер блока графики равен 22 линиям.
 ****************************************************************************/
public final class LoadGraphics3 extends PrinterCommand {
	// in

	private int password; // Operator password (4 bytes)
	private int lineLength; // Graphics line length
	private int lineNumber; // Graphics line number (2 bytes) 0…1199
	private int lineCount; // Graphics line count
	private int bufferType; // Graphics line count
	private byte[] data; // Graphical data (40 bytes)
	// out
	private int operator;

	/**
	 * Creates a new instance of LoadExtendedGraphics
	 */
	public LoadGraphics3() {
		super();
	}

	public final int getCode() {
		return 0x4E;
	}

	public final String getText() {
		return "Load graphics 512";
	}

	public final void encode(CommandOutputStream out) throws Exception {
		out.writeInt(getPassword());
		out.writeByte(getLineLength());
		out.writeShort(getLineNumber());
		out.writeShort(getLineCount());
		out.writeByte(getBufferType());
		out.writeBytes(getData());
	}

	public static int getMaxDataLength(int commandLength) {
		return commandLength - 10;
	}

	public final void decode(CommandInputStream in) throws Exception {
		setOperator(in.readByte());
	}

	public int getOperator() {
		return operator;
	}

	public int getPassword() {
		return password;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public byte[] getData() {
		return data;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public void setLineNumber(int lineNumber) throws Exception {
		this.lineNumber = lineNumber;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	public int getLineLength() {
		return lineLength;
	}

	public void setLineLength(int lineLength) {
		this.lineLength = lineLength;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public int getBufferType() {
		return bufferType;
	}

	public void setBufferType(int bufferType) {
		this.bufferType = bufferType;
	}
}
