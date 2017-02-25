package com.bradpritchard.robot.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bradpritchard.robot.command.handler.CommandHandler;

@Service
public class CommandDispatcher {

	@Autowired
	List<CommandHandler> commandHandlers;

	public void processCommand(Command command) {
		for (CommandHandler commandHandler : commandHandlers) {
			if (commandHandler.handleCommand(command)) {
				return;
			}
		}
	}
}
