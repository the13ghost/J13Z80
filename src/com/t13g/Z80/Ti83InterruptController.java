package com.t13g.Z80;

import com.t13g.Z80.core.InterruptController;
import com.t13g.Z80.core.InterruptDevice;
import com.t13g.Z80.core.PortDevice;

public class Ti83InterruptController implements PortDevice, InterruptController {

	private byte status = 8;
	private byte control = 0;
	
	public Ti83Keypad keypad;
	@Override
	public boolean containsPort(int r) {
		return r==3?true:false;
	}
	
	@Override
	public byte read(int port) {
		System.out.println("int test r");
		return status;
	}

	@Override
	public void write(int port, byte r) {
		System.out.println("int test w");
		control = r;
		status = (byte) (r|~7);
	}

	@Override
	public int check() {
		//checkKeyboard
		boolean testON = keypad.KBD_ON();
		status = (byte) ((testON?0:8)|
				((control&4)==4?4:0)|
				((control&2)==2?2:0)|
				((control&1)==1&&testON?1:0));
		int out = (status&7)>0?0xff:0xffff;
		return out;
	}

	@Override
	public void addInterrupter(InterruptDevice o) {
		keypad = (Ti83Keypad) o;
	}

}
