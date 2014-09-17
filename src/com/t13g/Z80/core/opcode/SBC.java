package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.ALU;
import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class SBC extends ExecutableCommand {

	public SBC(CPU core, Memory mem) {
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
		int buf;

		switch(op){
		case ExecutableCommand.R2R:
			src = core.registers[src]+(core.registers[CPU.F]&1);
			src &= 0xff;
			core.registers[dest] = ALU.sub8(core.registers[dest],
					(byte) src,
					core.registers);
			break;	
		case ExecutableCommand.I2R:
			src = core.memRead()+(core.registers[CPU.F]&1);
			src &= 0xff;
			core.registers[dest] = ALU.sub8(core.registers[dest],
					(byte) src,
					core.registers);
			ret += 3;
			break;
		case ExecutableCommand.RP2RP:
			buf = core.get16(src)+(core.registers[CPU.F]&1);
			buf = ALU.add16(core.get16(dest),-buf,core.registers);
			core.registers[dest+1] = (byte) (buf &255);
			core.registers[dest] = (byte) (buf>>8&255);
			core.registers[CPU.F] |= (1<<CPU.FLAG_N);
			ret += 11;
			break;
		case ExecutableCommand.MR2R:
			src = mem.read(core.get16(src))+(core.registers[CPU.F]&1);
			core.registers[dest] = ALU.sub8(core.registers[dest],
					(byte) src,
					core.registers);
			ret += 3;
			break;
		case ExecutableCommand.MR2R|ExecutableCommand.IX:
			buf = core.get16(CPU.IXH)+core.memRead();
			buf &= 0xffff;
			src = mem.read(buf)+(core.registers[CPU.F]&1);
			core.registers[dest] = ALU.sub8(core.registers[dest],
					(byte) src,
					core.registers);
			ret += 15;		
			break;
		case ExecutableCommand.MR2R|ExecutableCommand.IY:
			buf = core.get16(CPU.IYH)+core.memRead();
			buf &= 0xffff;
			src = mem.read(buf)+(core.registers[CPU.F]&1);
			core.registers[dest] = ALU.sub8(core.registers[dest],
					(byte) src,
					core.registers);
			ret += 15;
			break;
		}

		return ret;
	}

}
