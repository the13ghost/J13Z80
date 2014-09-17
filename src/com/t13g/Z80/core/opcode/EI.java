package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class EI extends ExecutableCommand {

	public EI(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		System.out.println("EI RAN");
		core.IFF1 = true;
		core.IFF2 = true;
		
		return 0;
	}

}
