package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class IM extends ExecutableCommand {

	public IM(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		// get operation
		int op = options & 0xFFFF;
		System.out.println("IM RAN "+ op);
		switch(op){
		case 0:
			core.IM0 = true;
			break;
		case 1:
			core.IM1 = true;
			break;
		case 2:
			core.IM2 = true;
			break;
		}
		return 4;
	}

}
