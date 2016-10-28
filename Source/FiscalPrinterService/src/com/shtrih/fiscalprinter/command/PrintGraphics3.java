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
 * ѕечать расширенной графики 512 с масштабированием1  оманда: 4DH. ƒлина
 * сообщени€: 12 байт. ѕароль оператора (4 байта) Ќачальна€ лини€ (2 байта)
 * 1Е600  онечна€ лини€ (2 байта) 1Е600  оэффициент масштабировани€ точки по
 * вертикали (1 байт) 1Е6  оэффициент масштабировани€ точки по горизонтали (1
 * байт) 1Е6 ‘лаги (1 байт) Ѕит 0 Ц контрольна€ лента2, Ѕит 1 Ц чекова€ лента,
 * Ѕит 23 Ц подкладной документ, Ѕит 34 Ц слип чек; Ѕит 75 Ц отложенна€ печать
 * графики ќтвет: 4DH. ƒлина сообщени€: 3 байта.  од ошибки (1 байт) ѕор€дковый
 * номер оператора (1 байт) 1Е30 ѕримечани€: 1 Ц в зависимости от модели   “
 * (дл€ параметра модели Ѕит 42, см. команду F7H); 2 Ц в зависимости от модели
 *   “ (дл€ параметра модели Ѕит 20, см. команду F7H); 3 Ц в зависимости от
 * модели   “ (дл€ параметра модели Ѕит 21, см. команду F7H); 4 Ц в зависимости
 * от модели   “ (дл€ параметра модели Ѕит 34, см. команду F7H); если Ѕит 7
 * установлен и фискальный чек открыт и установлена настройка
 * "ѕ≈„ј“№ „≈ ј ѕќ «ј –џ“»ё" в таблице 1, то графика будет распечатана перед
 * фискальным чеком; если не установлен Ѕит 7, то графика печатаетс€ немедленно;
 * результат печати можно проверить командой 10H; 5 Ц в зависимости от модели
 *   “ (дл€ параметра модели Ѕит 23, см. команду F7H).
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
