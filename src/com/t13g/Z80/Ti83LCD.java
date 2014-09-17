package com.t13g.Z80;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.t13g.Z80.core.PortDevice;

public class Ti83LCD implements PortDevice{

	public static final int AUTOINCREMENT = 0;
	public static final int CURRCOL = 1;
	public static final int OPSTATE = 4;
	public static final int ENABLED = 5;
	public static final int FULLBYTE = 6;
	public static final int BUSY = 7;
	
	public byte[] buffer = new byte[16*64];
	
	private int status;
	private int col;
	private int row;
	private boolean delay;
	private int scroll;
	private int contrast;
	
	
	@Override
	public boolean containsPort(int r) {
		if(r==0x10 || r == 0x11 || r==0x12 || r==0x13)
			return true;
		else
			return false;
	}

	@Override
	public byte read(int port) {
		System.out.println("test r");
		if(port==0x10){
			return (byte) (status&255);
		}
		else if(port==0x11){
			if (delay){
				delay = false;
				return 0;
			}
			int width, out, coord;
			if((1<<FULLBYTE)==(status&(1<<FULLBYTE))){
				width = 14;
				out = col>width? 0: buffer[row*16+col];
			} else {
				width = 19;
				if(col>width ) {
					out = 0;
				} else {
					out = col*6;
					coord = row*16+(out>>3);
					out &= 7;
					out = ((buffer[coord]<<out)|(buffer[coord+1]>>(8-out)))>>2;
				}
			}
			switch(status&0x3){
			case 0:
				row = row!=0?row-1:63;
				break;
			case 1:
				row = row<63?row+1:0;
				break;
			case 2:
				col = (col!=0?col-1:width)&0x1F;
				break;
			case 3:
				col = (col==width?0:col+1)&0x1F;
				break;
			}
			return (byte) out;
		} else 
			return 0;
	}

	@Override
	public void write(int port, byte r) {
		System.out.println("test w");
		if(port==0x10){
			delay = true;
			if(r>=0xC0)
				contrast = r&0x3f;
			else if(r >= 0x80)
				row = r&0x3f;
			else if(r >= 0x40)
				scroll = r &0x3f;
			else if(r >= 0x20)
				col = r&0x3f;
			else switch(r){
			case 0:
				status &= ~(1<<FULLBYTE);
				break;
			case 1:
				status |= (1<<FULLBYTE);
				break;
			case 2:
				status &= ~(1<<ENABLED);
				break;
			case 3:
				status |= (1<<ENABLED);
				break;
			case 4:
			case 5:
			case 6:
			case 7:
				status = (status&0xFC)|(r-4);
				break;
			}
		}
		else if(port==0x11){
			int width;
			if((1<<FULLBYTE)==(status&(1<<FULLBYTE))){
				width = 14;
				if(col <= width)
					buffer[row*16+col] = r;
			} else {
				width = 19;
				if(col <= width){
					int over = col*6;
					int coord = row*16+(over>>3);
					over &= 0x07;
					buffer[coord] = (byte) ((buffer[coord] &~(0xFC>>over)) |
									(r<<2)>>over);	
					
					if(over != 0){
						over = 8 - over;
						coord++;
						buffer[coord] = (byte) ((buffer[coord] &~(0xFC<<over)) |
										(r<<2)<<over);	
					}
				}
			}
			switch(status&0x3){
			case 0:
				row = row!=0?row-1:63;
				break;
			case 1:
				row = row<63?row+1:0;
				break;
			case 2:
				col = (col!=0?col-1:width)&0x1F;
				break;
			case 3:
				col = (col==width?0:col+1)&0x1F;
				break;
			}
		}
	}

	public BufferedImage getBuffer() {
		BufferedImage out = new BufferedImage(96,64,BufferedImage.TYPE_INT_RGB);
		int[] pixBuf = ((DataBufferInt) out.getRaster().getDataBuffer()).getData();;
		for (int y=0; y<64; y++){
			for(int x=0; x<12;x++){
				for(int i=0;i<8;i++){
					int b = buffer[y*16+x];
					b>>=(7-i);
					b&=1;
					pixBuf[(x*8+i)+y*96] = b==1?0x00:0x9CA886;
				}
			}
		}
		return out;
	}

}
