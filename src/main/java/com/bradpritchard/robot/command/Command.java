package com.bradpritchard.robot.command;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Command {

	@NonNull
	private String commandText;
}
