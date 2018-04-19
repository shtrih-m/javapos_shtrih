package com.shtrih.fiscalprinter.commands;

import com.shtrih.fiscalprinter.command.FlexCommand;
import com.shtrih.fiscalprinter.command.FlexCommands;
import com.shtrih.fiscalprinter.command.FlexCommandsReader;

import org.junit.Ignore;
import org.junit.Test;

public class XmlTest {

    @Test
    @Ignore
    public void xml_serialization_is_correct() throws Exception {
        FlexCommands commands = new FlexCommands();
        FlexCommandsReader reader = new FlexCommandsReader();
        reader.load(commands);

        for (FlexCommand cmd : commands.list()) {
            if(cmd.getTimeout() == 10000)
                continue;

            System.out.println("case 0x" + Integer.toString(cmd.getCode(),16) + ":");
            System.out.println("return " + cmd.getTimeout()  +";");

        }
    }
}