package com.t13g.Z80.core;

public interface Memory {
	
	
	public byte read(int p);
	
	public void write8(int p, int val);
	
	public void write16(int p, int r,int r2);
	
}
