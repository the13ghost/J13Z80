package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class SET extends ExecutableCommand {

	public SET(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int op = options&0xffff;
		int src = options>>>16& 0xFF;
		int dest = options>>>24& 0xFF;
		int buf;

		switch(op) {
		case ExecutableCommand.MR2MR:
			src = mem.read(core.get16(CPU.H));
			src |= (1<<dest);
			mem.write8(core.get16(CPU.H), src);
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IX:
			buf =core.get16(CPU.IXH)+core.memRead();
			buf&=0xffff;
			src = mem.read(buf);
			src |= (1<<dest);
			mem.write8(buf, src);
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IY:
			buf = core.get16(CPU.IYH)+core.memRead();
			buf&=0xffff;
			src = mem.read(buf);
			src |= (1<<dest);
			mem.write8(buf, src);
			break;
		case ExecutableCommand.R2R:
			core.registers[src] |= (1<<dest);
			break;
		}

		return 0;
	}

}
