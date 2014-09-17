package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.ALU;
import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class BIT extends ExecutableCommand {

	public BIT(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int op = options&0xffff;
		int src = options>>>16& 0xFF;
		int dest = options>>>24& 0xFF;
		int buf = 0;
		switch(op) {
		case ExecutableCommand.MR2MR:
			src = mem.read(core.get16(CPU.H));
			// z is bit
			// h is set
			// n is reset
			// c is same
			ALU.bit8((byte)src,(byte)dest,core.registers);
			break;
			
		case ExecutableCommand.MR2MR|ExecutableCommand.IX:
			buf = core.get16(CPU.IXH)+core.memRead();
			buf &= 0xffff;
			src = mem.read(buf);
			ALU.bit8((byte)src,(byte)dest,core.registers);
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IY:
			buf = core.get16(CPU.IYH)+core.memRead();
			buf &= 0xffff;
			src = mem.read(buf);
			ALU.bit8((byte)src,(byte)dest,core.registers);
			break;
		case ExecutableCommand.R2R:
			src = core.registers[src];
			ALU.bit8((byte)src,(byte)dest,core.registers);
			break;
		}
		
		return 0;
	}

}
