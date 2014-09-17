package com.t13g.Z80;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.t13g.Z80.core.InterruptDevice;
import com.t13g.Z80.core.PortDevice;

public class Ti83Keypad implements PortDevice, InterruptDevice, KeyListener{

	private volatile boolean kbd_on;
	@Override
	public boolean containsPort(int r) {
		return r==1?true:false;
	}

	@Override
	public byte read(int port) {
		// TODO Auto-generated method stub
		System.out.println("\n\n\nREADING\n\n\n");
		return (byte)0xff;
	}

	@Override
	public void write(int port, byte r) {
		System.out.println("\n\n\nWriting\n\n\n");
	}

	public boolean KBD_ON() {
		//if on is pressed
		return kbd_on;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == 'q') KBD_ON(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar() == 'q') KBD_ON(false);
	}

	private void KBD_ON(boolean b) {
		// TODO Auto-generated method stub
		kbd_on = b;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
