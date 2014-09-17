package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class IY extends ExecutableCommand {

	public IY(CPU core, Memory mem) {
		super(core, mem);
		
	}

	@Override
	public int execute(int options) {
		int ret = 0;
		int opcode = core.memRead()&0xff;
		ExecutableCommand ope = core.opcodesTable[opcode];
		int opss = core.optionsTable[opcode]|ExecutableCommand.IY;
		ret = ope.execute(opss);
		return ret;
	}

}
