package com.t13g.Z80;

import com.t13g.Z80.core.PortDevice;

public class Ti83MemoryController implements PortDevice {
	
	public byte link;
	public byte rompage;
	public byte power;
	public Ti83Memory mem;
	public Ti83MemoryController(Ti83Memory mem) {
		this.mem = mem;
		link = 0x0C;
		//0000 1100
		//0
		rompage = (byte) 0x99;
		//1001 1001
		//rom page, 001
		power = 0x00;
		mem.mapper(link,rompage,power);
	}

	@Override
	public boolean containsPort(int r) {
		return r==2?true:r==0?true:r==4?true:false;
	}

	@Override
	public byte read(int port) {
		System.out.println("mem test r port:" + port + " value " + (port==0?link:port==2?rompage:port==4?power:0xff));
		return (byte) (port==0?link:port==2?rompage:port==4?power:0xff);
	}

	@Override
	public void write(int port, byte r) {
		System.out.println("mem test w port:"+port+" value " + r);
		switch(port){
		case 0:
			link = (byte) (((r^0x03)|0x0C)&0x1F);
			break;
		case 2:
			rompage = r;
			break;
		case 4:
			power = r;
			break;
		}
		
		// call mapper
		mem.mapper(link,rompage,power);
	}

}
