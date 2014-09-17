package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class DI extends ExecutableCommand {

	public DI(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		System.out.println("DI ran");
		core.IFF1 = false;
		core.IFF2 = false;
		return 0;
	}

}
