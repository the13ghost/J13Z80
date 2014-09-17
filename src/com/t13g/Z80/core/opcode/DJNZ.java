package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class DJNZ extends ExecutableCommand {

	public DJNZ(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		core.registers[CPU.B]--;
		if(core.registers[CPU.B]!=0){
			core.PC += core.memRead();
			core.PC &= 0xffff;
			core.memRead();
		} else{
			core.memRead();
		}

		return 0;
	}

}
