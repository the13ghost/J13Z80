package com.t13g.Z80;

import com.t13g.Z80.core.Memory;

public class Ti83Memory implements Memory {

	private byte[] ram = new byte[32768];
	private byte[] rom = new byte[262144];
	private int[] pages = new int[4];
	private byte[] map = new byte[65536];
	
	public void loadProgram(String program){
		int i=0;
		for(String s : program.split("(?<=\\G.{2})"))
			map[i++%map.length]=(byte) Integer.parseInt(s, 16);	
	}
	
	public void loadProgram(byte[] program){
		for(int i=0;i<program.length;i++)
			map[i]=program[i];
	}
	
	
	public void loadPage(int pageNum, int pageID){
		byte[] tempPage = new byte[0x4000];
		int j;
		if(pageID>15){
			j = (pageID-16)*0x4000;
			for(int i=0; i < 0x4000; i++){
				tempPage[i] = map[pageNum*0x4000 + i];
			}
			for(int i=0; i < 0x4000; i++){
				map[pageNum*0x4000 + i] = ram[j + i];
			}
			if(pages[pageNum]>15)
				for(int i=0; i < 0x4000; i++)
					ram[(pages[pageNum]-16)*0x4000+i] = tempPage[i];
			
			pages[pageNum] = pageID; 
		} else {
			j = (pageID)*0x4000;
			for(int i=0; i < 0x4000; i++){
				tempPage[i] = map[pageNum*0x4000 + i];
			}
			for(int i=0; i < 0x4000; i++){
				map[pageNum*0x4000 + i] = rom[j+i];
			}
			if(pages[pageNum]>15)
				for(int i=0; i < 0x4000; i++)
					ram[(pages[pageNum]-16)*0x4000+i] = tempPage[i];
			
			pages[pageNum] = pageID; 
		}
		return;
	}
	
	public byte read(int p){
		return map[p%map.length];
	}
	
	public void write8(int p, int val){
		map[p%map.length]=(byte) (val&255);
	}
	
	public void write16(int p, int r,int r2){
		map[p++%map.length]=(byte) r2;
		map[p%map.length]=(byte) r;
	}

	public void mapper(byte link, byte rompage, byte power) {
		//loadPage(0, 0);
		// if rompagebit 6==0 rom else ram
		// bit 0-2 from rompage | bit 3 from bit 4 on link 
		// swap = rompagebit6==0?16:0;
		// swap = swap + rompagebit6==0?bit0:bit0-2rompage|bit4link
		int swap = (rompage&0x40)==0x40?16:0;
		swap += (rompage&0x40)==0x40?rompage&1:(rompage&7)|(link>>1&8);
		
		int buf = (rompage>>6&2)|(rompage>>3&1);
		if((power&1)==0){
			switch(buf){
			case 0:
				loadPage(0,0);
				loadPage(1,swap);
				loadPage(2,(link&0x10)==0x10?8:0);
				loadPage(3,16);
				break;
			case 1:
				loadPage(0,0);
				loadPage(1,swap);
				loadPage(2,(link&0x10)==0x10?9:1);
				loadPage(3,16);
				break;
			case 2:
				loadPage(0,0);
				loadPage(1,swap);
				loadPage(2,16);
				loadPage(3,16);
				break;
			case 3:
				loadPage(0,0);
				loadPage(1,swap);
				loadPage(2,17);
				loadPage(3,16);
				break;
			}
				
		} else if((rompage&0x40) == 0x40) {
			switch(buf){
			case 0:
				loadPage(0,0);
				loadPage(1,(link&0x10)==0x10?8:0);
				loadPage(2,swap);
				loadPage(3,(link&0x10)==0x10?8:0);
				break;
			case 1:
				loadPage(0,0);
				loadPage(1,(link&0x10)==0x10?8:0);
				loadPage(2,swap);
				loadPage(3,(link&0x10)==0x10?9:1);
				break;
			case 2:
				loadPage(0,0);
				loadPage(1,(link&0x10)==0x10?8:0);
				loadPage(2,swap);
				loadPage(3,16);
				break;
			case 3:
				loadPage(0,0);
				loadPage(1,(link&0x10)==0x10?8:0);
				loadPage(2,swap);
				loadPage(3,17);
				break;
			}
		} else {
			switch(buf){
			case 0:
				loadPage(0,0);
				loadPage(1,16);
				loadPage(2,17);
				loadPage(3,(link&0x10)==0x10?8:0);
				break;
			case 1:
				loadPage(0,0);
				loadPage(1,16);
				loadPage(2,17);
				loadPage(3,(link&0x10)==0x10?9:1);
				break;
			case 2:
				loadPage(0,0);
				loadPage(1,16);
				loadPage(2,17);
				loadPage(3,16);
				break;
			case 3:
				loadPage(0,0);
				loadPage(1,16);
				loadPage(2,17);
				loadPage(3,17);
				break;
			}
		}
		
	}

	public void loadRom(byte[] tmp) {
		for (int i=0; i<rom.length;i++){
			rom[i]=0;
		}
		for (int i=0; i<tmp.length;i++){
			rom[i]=tmp[i];
		}
	}

}
