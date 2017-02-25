package com.bradpritchard.robot.command.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bradpritchard.robot.command.Command;
import com.bradpritchard.robot.command.CommandDispatcher;
import com.bradpritchard.robot.command.adapter.OralCommandAdapter;

@RestController
@RequestMapping("/command")
public class CommandController {

	@Autowired
	private CommandDispatcher commandDispatcher;
	
	@Autowired
	private OralCommandAdapter oralCommandAdapter;

	@RequestMapping(path = "/execute", method = RequestMethod.GET)
	@ResponseBody
	public String executeCommand(@RequestParam String command) {
		commandDispatcher.processCommand(new Command(command));
		return "ok";
	}

	@RequestMapping(path = "/listen", method = RequestMethod.GET)
	@ResponseBody
	public String listenForOralCommands() {
		oralCommandAdapter.enable();
		return "ok";
	}
}
