package com.t13g.Z80;

import java.util.Scanner;

import com.t13g.Z80.core.PortDevice;

public class ConsoleDebug implements PortDevice{
	public static final int[] ports = { 0, 1, 2};
	String line;
	boolean read = false;
	public ConsoleDebug() {
	}
	
	public byte read(int port){
		if(port == 2){
			if(line.length()>0)
				return 1;
		}
		
		if(port == 1){
			char out = line.charAt(0);
			line = line.substring(1);
			return (byte) (out&0xff);
		}
		return 0;
	}
	
	public void write(int p,byte r){
		if(p==1){
			read = true;
			Scanner input = new Scanner(System.in);
			line = input.nextLine();
		}
		if(p==0)
			System.out.print((char)r);
	}

	@Override
	public boolean containsPort(int r) {
		for(int i=0;i<ports.length;i++)
			if(r == ports[i])
				return true;
		return false;
	}
}
