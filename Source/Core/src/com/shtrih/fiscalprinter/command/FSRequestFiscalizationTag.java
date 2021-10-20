package com.shtrih.fiscalprinter.command;

/**
 * Запрос параметра открытия ФН
 * Код команды FF0Eh . Длина сообщения: 9 байт.
 * Пароль системного администратора: 4 байта
 * Порядковый номер отчета о регистрации/перерегистрации: 1 байт
 * Номер тега (Тип Т, TLV параметра): 2 байта (если T=FFFFh2, то читать TLV структуру командой FF3Bh)
 * Ответ:    FF0Eh Длина сообщения: 2+1+X1 байт.
 * Код ошибки : 1 байт
 * TLV структура: X1 байт
 * Примечание:
 * 1 - длина ответного сообщения зависит от TLV структуры, возвращаемой ФН на заданный номер тега (кроме FFFFh);
 * 2 - при запросе всех тегов TLV структура не возвращается (X=0).
 */
public class FSRequestFiscalizationTag extends PrinterCommand {

    private final int sysPassword;
    private final int fiscalizationNumber;
    private final int tagNumber;

    // out
    private byte[] tagData;

    public FSRequestFiscalizationTag(int sysPassword, int fiscalizationNumber, int tagNumber) {
        this.sysPassword = sysPassword;
        this.fiscalizationNumber = fiscalizationNumber;
        this.tagNumber = tagNumber;
    }

    public final int getCode() {
        return 0xFF0E;
    }

    public final String getText() {
        return "Fiscal storage: read fiscalization tag";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
        out.writeByte(fiscalizationNumber);
        out.writeShort(tagNumber);
    }

    public void decode(CommandInputStream in) throws Exception {
        tagData = in.readBytesToEnd();
    }

    public byte[] getTagData() {
        return tagData;
    }
}
