package com.t13g.Z80.core;

public interface PortDevice {
	public boolean containsPort(int r);
	
	public byte read(int port);
	
	public void write(int port,byte r);
}
