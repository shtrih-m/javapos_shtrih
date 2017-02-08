package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author P.Zhirkov
 */
public class FlexCommandsTests {
    @Test
    public void Should_return_null_when_command_was_not_found() {
        FlexCommands commands = new FlexCommands();
        assertNull(commands.itemByCode(1));
    }

    @Test
    public void Should_store_and_return_command_by_code() {
        FlexCommands commands = new FlexCommands();
        
        FlexCommand cmd1 = new FlexCommand();
        cmd1.setCode(1);

        commands.add(cmd1);

        FlexCommand cmd2 = new FlexCommand();
        cmd2.setCode(2);

        commands.add(cmd2);

        assertEquals(cmd1, commands.itemByCode(1));
        assertEquals(cmd2, commands.itemByCode(2));
    }

    @Test
    public void Should_report_size() {
        FlexCommands commands = new FlexCommands();

        assertEquals(0,commands.size());

        FlexCommand cmd1 = new FlexCommand();
        cmd1.setCode(11);

        commands.add(cmd1);

        assertEquals(1,commands.size());

        FlexCommand cmd2 = new FlexCommand();
        cmd2.setCode(22);

        commands.add(cmd2);

        assertEquals(2,commands.size());
    }
}
