package com.t13g.Z80.core;

public class ALU {
	public static int[] addHalfCarryTable={0,0,1,0,1,0,1,1};
	public static int[] subHalfCarryTable={0,1,1,1,0,0,0,1};
	public static int[] addOverflowTable ={0,1,0,0,0,0,1,0};
	public static int[] subOverflowTable ={0,0,0,1,1,0,0,0};

	public static final int FLAG_S = 7;
	public static final int FLAG_Z = 6;
	public static final int FLAG_5 = 5;
	public static final int FLAG_H = 4;
	public static final int FLAG_3 = 3;
	public static final int FLAG_PV = 2;
	public static final int FLAG_N = 1;
	public static final int FLAG_C = 0;

	/*
	 * ADD
	 */
	public static byte add8(byte r, byte r2, byte[] registers){
		int buf = r+r2;
		int flags = ((r&0x88)>>1)|
					((r2&0x88)>>2)|
					((buf&0x88)>>3);
		registers[CPU.F]=(byte)((buf&0x80)|
					(buf==0?1<<FLAG_Z:0)|
					(ALU.addHalfCarryTable[flags&7]<<FLAG_H)|
					(ALU.addOverflowTable[(flags>>4)&7]<<FLAG_PV)|
					(((buf&0x0100)>0)?1:0));
		return (byte) (buf&0xff);
	}
	public static short add16(int a, int b, byte[] registers) {
		byte f = registers[CPU.F];
		int buf = a+b;
		int flags = ((a&0x8800)>>9)|
					((b&0x8800)>>11)|
					((buf&0x8800)>>12);
		registers[CPU.F] = (byte) (
					(f&(1<<FLAG_S))|
					(f&(1<<FLAG_Z))|
					(ALU.addHalfCarryTable[flags&7]<<FLAG_H)|
					(f&(1<<FLAG_PV))|
					(0<<FLAG_N)|
					((buf>0xffff)?1:0)); 
		return (short) (buf&0xffff);
	}
	
	/*
	 * SUB
	 */
	public static byte sub8(byte r, byte r2, byte[] registers){
		int buf = r-r2;
		int flags = ((r&0x88)>>1)|
					((r2&0x88)>>2)|
					((buf&0x88)>>3);
		registers[CPU.F]=(byte)((buf&0x80)|
				(buf==0?1<<6:0)|
				(subHalfCarryTable[flags&7]<<4)|
				(subOverflowTable[(flags>>4)&7]<<2)|
				(1<<FLAG_N)|
				(((buf&0x0100)>0)?1:0));
		return (byte) (buf&255);
	}

	public static void cpd8(byte r, byte r2, byte[] registers){
		int buf = r-r2;
		int flags = ((r&0x88)>>1)|
					((r2&0x88)>>2)|
					((buf&0x88)>>3);
		
		registers[CPU.F]=(byte)((buf&0x80)|
				((buf==0)?1<<6:0)|
				(subHalfCarryTable[flags&7]<<4)|
				(1<<FLAG_N)|
				(registers[CPU.F]&1));
	}
	/*
	 * XOR
	 */
	public static byte xor8(byte r, byte r2, byte[] registers) {
		int buf = r^r2;
		int parity =0;
		for(int i=7;i>=0;i--)
			parity+=((buf&(1<<i))>>i);
		registers[CPU.F]=(byte)((buf&0x80)|
							(buf==0?1<<FLAG_Z:0)|
							((parity&1)==0?1<<FLAG_PV:0));
		return (byte) (buf&0xff);
	}
	
	/*
	 * OR
	 */
	public static byte or8(byte r, byte r2, byte[] registers) {
		int buf = r|r2;
		int parity =0;
		for(int i=7;i>=0;i--)
			parity+=((buf&(1<<i))>>i);
		registers[CPU.F]=(byte)((buf&0x80)|
							(buf==0?1<<FLAG_Z:0)|
							((parity&1)==0?1<<FLAG_PV:0));
		return (byte) (buf&0xff);
	}
	
	/*
	 * SRL
	 */
	public static byte srl8(byte r, byte[] registers) {
		byte f = registers[CPU.F];
		registers[CPU.F] = (byte) (
						(f&(1<<FLAG_S))|
						(f&(1<<FLAG_Z))|
						(f&(1<<FLAG_PV))|
						((r&1)==1?1:0)); 
		return (byte) ((r>>>1)&0xff);
	}
	
	/*
	 * SRA
	 */
	public static byte sra8(byte r, byte[] registers) {
		byte f = registers[CPU.F];
		registers[CPU.F] = (byte) (
						(f&(1<<FLAG_S))|
						(f&(1<<FLAG_Z))|
						(f&(1<<FLAG_PV))|
						((r&1)==1?1:0)); 
		return (byte) ((r>>1)&0xff);
	}
	
	/*
	 * AND
	 */
	public static byte and8(byte r, byte r2, byte[] registers){
		int buf = r&r2;
		int parity =0;
		for(int i=7;i>=0;i--)
			parity+=((buf&(1<<i))>>i);
		registers[CPU.F]=(byte)((buf&0x80)|
							(buf==0?1<<FLAG_Z:0)|
							(1<<FLAG_H)|
							((parity&1)==0?1<<FLAG_PV:0));
		return (byte) (buf&0xff);
	}
	
	/*
	 * BIT
	 */
	public static void bit8(byte r, byte r2, byte[] registers){
		registers[CPU.F] &= ~(1<<CPU.FLAG_Z);
		registers[CPU.F] |= 1<<CPU.FLAG_H;
		registers[CPU.F] |= ((r&(1<<r2))>>r2)<<CPU.FLAG_Z;
		registers[CPU.F] &= ~(1<<CPU.FLAG_N);
	}
}
