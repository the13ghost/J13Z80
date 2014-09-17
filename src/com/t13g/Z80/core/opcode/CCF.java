package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class CCF extends ExecutableCommand {

	public CCF(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		//s Z PV =
		//H prev C
		// N 0
		// c 1 if carry flag was 0 else 0
		boolean cf = (core.registers[CPU.F]&1)==1?true:false;
		core.registers[CPU.F] &= ~(1<<CPU.FLAG_H);
		core.registers[CPU.F] |= (cf?1:0)<<CPU.FLAG_H;
		core.registers[CPU.F] &= (1<<CPU.FLAG_S)|(1<<CPU.FLAG_Z)|
								 (1<<CPU.FLAG_PV)|(1<<CPU.FLAG_H);
		core.registers[CPU.F] |= cf?0:1;
		return 0;
	}

}
