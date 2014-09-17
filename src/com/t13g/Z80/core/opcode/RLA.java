package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class RLA extends ExecutableCommand {

	public RLA(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int c = (core.registers[CPU.A]&(1<<7))>>7;
		core.registers[CPU.A]<<=1;
		core.registers[CPU.A]|=core.registers[CPU.F];
		core.registers[CPU.F]=(byte)((core.registers[CPU.F]&~1)|(c));
		return 0;
	}

}
