package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class RLCA extends ExecutableCommand {

	public RLCA(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int op = options&0xffff;
		int src = CPU.A;
		int c;
		
		c = (core.registers[src]&(1<<7))>>7;
		core.registers[src]<<=1;
		core.registers[src]|=c;
		core.registers[CPU.F]=(byte)((core.registers[CPU.F]&~1)|(c));
		
		return 0;
	}

}
