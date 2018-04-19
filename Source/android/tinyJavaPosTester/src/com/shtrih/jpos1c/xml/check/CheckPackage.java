package com.shtrih.jpos1c.xml.check;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;

/**
 * Текст в формате XML содержит описание передаваемого для формирования чека.
 * Структура описывает параметры и последовательность формирования фискального чека.
 */
@Root(name = "CheckPackage")
public class CheckPackage {

    /**
     * Формирование нового чека с заданным атрибутами
     */
    @Element
    public Parameters Parameters;

    /**
     * Формирование нового чека с заданным атрибутами
     */
    @Element
    public Positions Positions;

    /**
     * Параметры закрытия чека. Чек коррекции может быть оплачен только одним видом оплаты
     * и без сдачи.
     */
    @Element
    public Payments Payments;

    public CheckPackage(){
        Parameters = new Parameters();
        Positions = new Positions();
        Payments = new Payments();
    }

    public String toXml() throws Exception {

        Serializer serializer = new Persister();

        StringWriter writer = new StringWriter();
        serializer.write(this, writer);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + writer.toString();
    }
}
