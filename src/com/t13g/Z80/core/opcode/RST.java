package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class RST extends ExecutableCommand {

	public RST(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int op = options & 0xFFFF;
		int dest = core.get16(CPU.SPH);
		dest = (dest-1)&0xffff;
		mem.write8(dest, core.PC >> 8 &255);
		dest = (dest-1)&0xffff;
		mem.write8(dest, core.PC&255);
		core.PC = op;
		core.registers[CPU.SPL] = (byte)(dest & 0xff);
		core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
		return 6;
	}

}
