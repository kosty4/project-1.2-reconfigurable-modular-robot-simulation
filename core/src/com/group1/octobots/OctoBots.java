package com.group1.octobots;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.group1.octobots.rendering.MenuScreen;

public class OctoBots extends Game {

	/**
	 * Called when the {@link Application} is first created.
	 */
	@Override
	public void create() {
		World.application = this;
		// Set the first screen
		setScreen(new MenuScreen());
	}
}
