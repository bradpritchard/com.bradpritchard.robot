package com.bradpritchard.robot.command.handler;

import com.bradpritchard.robot.command.Command;

public interface CommandHandler {

	boolean handleCommand(Command command);
}
