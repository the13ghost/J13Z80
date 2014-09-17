package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class SCF extends ExecutableCommand {

	public SCF(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		byte f = core.registers[CPU.F];
		core.registers[CPU.F] = (byte) ((f&(1<<CPU.FLAG_Z))|
								(f&(1<<CPU.FLAG_S))|
								(f&(1<<CPU.FLAG_PV))|
								1);
		return 0;
	}

}
