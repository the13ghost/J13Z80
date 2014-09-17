package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.ALU;
import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class NEG extends ExecutableCommand {

	public NEG(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int dest = CPU.A;
		core.registers[dest] = ALU.sub8((byte) 0,
										core.registers[dest],
										core.registers);
		return 4;
	}

}
