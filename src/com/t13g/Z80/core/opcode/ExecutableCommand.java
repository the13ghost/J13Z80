package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public abstract class ExecutableCommand {


	public static final int NONE  = 0;
	public static final int R2MR  = 1;
	public static final int I2MR  = 2;
	public static final int R2MI  = 3;
	public static final int RP2MI = 4;
	public static final int MR2R  = 5;
	public static final int MI2R  = 6;
	public static final int R2R   = 7;
	public static final int MI2RP = 8;
	public static final int I2RP  = 9;
	public static final int I2R   = 10;
	public static final int RP2RP = 11;
	public static final int MR2MR = 12;
	public static final int RP2MR = 12;
	public static final int Z     = 13;
	public static final int NZ    = 14;
	public static final int C     = 15;
	public static final int NC    = 16;
	public static final int M     = 17;
	public static final int P     = 20;
	public static final int PO    = 18;
	public static final int PE    = 19;
	public static final int IX    = 512;
	public static final int IY    = 256;
	
	
	protected CPU core;
	protected Memory mem;
	public ExecutableCommand(CPU core, Memory mem){
		this.core = core;
		this.mem = mem;
	}
	public abstract int execute(int options);
	

}
