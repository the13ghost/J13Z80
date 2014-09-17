package com.t13g.Z80.core;

public interface InterruptController {
	public int check();
	public void addInterrupter(InterruptDevice o);
}
