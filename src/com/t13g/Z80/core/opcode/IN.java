package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class IN extends ExecutableCommand {

	public IN(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		// get operation
		int op = options & 0xFFFF;
		// get source
		int src = options >> 16 & 0xFF;
		// get dest
		int dest = options >> 24 & 0xFF;
		// default return
		int ret = 0;

		switch(op){
		case ExecutableCommand.I2R:
			core.registers[dest] = core.in8(core.memRead());
			ret += 7;
			break;
		case ExecutableCommand.R2R:
			core.registers[dest] = core.in8((byte) src);
			ret += 8;
			break;
		}

		return ret;
	}

}
