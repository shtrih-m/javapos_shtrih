package com.shtrih.fiscalprinter.command;

/**
Сгенерировать токен Mono FF53H
Код команды FF53h . Длина сообщения: 2 байта.

Ответ: FF53h Длина сообщения: 13 байт.
Код ошибки: 1 байт
    Токен: 10 байт
*/
public class GenerateMonoTokenCommand extends PrinterCommand {

    // in

    // out
    private String token;

    public GenerateMonoTokenCommand() {
    }

    public final int getCode() {
        return 0xFF53;
    }

    public final String getText() {
        return "Generate Mono token";
    }

    public void encode(CommandOutputStream out) throws Exception {

    }

    public void decode(CommandInputStream in) throws Exception {
        token = in.readString();
    }

    public String getToken() {
        return token;
    }
}
