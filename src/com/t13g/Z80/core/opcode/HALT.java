package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class HALT extends ExecutableCommand {

	public HALT(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		if(!core.halted) {
			System.out.println("halted");
			//System.out.println(core);
		}
		core.halted = true;
		core.PC--;
		core.PC&=0xffff;
		return 0;
	}

}
