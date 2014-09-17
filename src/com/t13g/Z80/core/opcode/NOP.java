package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class NOP extends ExecutableCommand {

	public NOP(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		return 0;
	}

}
