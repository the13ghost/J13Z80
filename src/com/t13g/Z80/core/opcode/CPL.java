package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class CPL extends ExecutableCommand {

	public CPL(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		core.registers[CPU.A]=(byte) ~core.registers[CPU.A];
		int buf = (1<<CPU.FLAG_N)|(1<<CPU.FLAG_H);
		int f = core.registers[CPU.F];
		core.registers[CPU.F]=(byte) (f|buf);
		return 0;
	}

}
