package com.bradpritchard.robot.command.adapter;

import org.springframework.beans.factory.annotation.Autowired;

import com.bradpritchard.robot.command.CommandDispatcher;

public abstract class CommandAdapter<T> {

	@Autowired
	protected CommandDispatcher commandDispatcher;

	public abstract void enable();

	public abstract void disable();
}
