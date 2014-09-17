package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class RRCA extends ExecutableCommand {

	public RRCA(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		//TODO:FLAGS
		int src = CPU.A;
		int c;

		c = (core.registers[src]&1);
		core.registers[src]>>>=1;
		core.registers[src]|=c<<7;
		core.registers[CPU.F]=(byte)((core.registers[CPU.F]&~1)|(c));

		return 0;
	}

}
