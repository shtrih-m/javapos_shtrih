/*
 * PrintExtendedGraphics.java
 *
 * Created on April 2 2008, 21:27
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
 * Печать расширенной графики 512 с масштабированием1 Команда: 4DH. Длина
 * сообщения: 12 байт. Пароль оператора (4 байта) Начальная линия (2 байта)
 * 1…600 Конечная линия (2 байта) 1…600 Коэффициент масштабирования точки по
 * вертикали (1 байт) 1…6 Коэффициент масштабирования точки по горизонтали (1
 * байт) 1…6 Флаги (1 байт) Бит 0 – контрольная лента2, Бит 1 – чековая лента,
 * Бит 23 – подкладной документ, Бит 34 – слип чек; Бит 75 – отложенная печать
 * графики Ответ: 4DH. Длина сообщения: 3 байта. Код ошибки (1 байт) Порядковый
 * номер оператора (1 байт) 1…30 Примечания: 1 – в зависимости от модели ККТ
 * (для параметра модели Бит 42, см. команду F7H); 2 – в зависимости от модели
 * ККТ (для параметра модели Бит 20, см. команду F7H); 3 – в зависимости от
 * модели ККТ (для параметра модели Бит 21, см. команду F7H); 4 – в зависимости
 * от модели ККТ (для параметра модели Бит 34, см. команду F7H); если Бит 7
 * установлен и фискальный чек открыт и установлена настройка
 * "ПЕЧАТЬ ЧЕКА ПО ЗАКРЫТИЮ" в таблице 1, то графика будет распечатана перед
 * фискальным чеком; если не установлен Бит 7, то графика печатается немедленно;
 * результат печати можно проверить командой 10H; 5 – в зависимости от модели
 * ККТ (для параметра модели Бит 23, см. команду F7H).
 ****************************************************************************/

public final class PrintGraphics3 extends PrinterCommand {
	// in
	private int password; // Operator password
	private int line1; // Number of first line
	private int line2; // Number of last line
	private int vscale;
	private int hscale;
	private int flags;
	// out
	private int operator = 0;

	/**
	 * Creates a new instance of PrintExtendedGraphics
	 */
	public PrintGraphics3() throws Exception {
	}

	public final int getCode() {
		return 0x4D;
	}

	public final String getText() {
		return "Print graphics 512";
	}

	public final void encode(CommandOutputStream out) throws Exception {
		out.writeInt(getPassword());
		out.writeShort(getLine1());
		out.writeShort(getLine2());
		out.writeByte(getVscale());
		out.writeByte(getHscale());
		out.writeByte(getFlags());
	}

	public final void decode(CommandInputStream in) throws Exception {
		operator = in.readByte();
	}

	public int getOperator() {
		return operator;
	}

	public int getLine1() {
		return line1;
	}

	public void setLine1(int line1) {
		this.line1 = line1;
	}

	public int getLine2() {
		return line2;
	}

	public void setLine2(int line2) {
		this.line2 = line2;
	}

	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public int getVscale() {
		return vscale;
	}

	public void setVscale(int vscale) {
		this.vscale = vscale;
	}

	public int getHscale() 
        {
		return hscale;
	}

	public void setHscale(int hscale) {
		this.hscale = hscale;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}
}
