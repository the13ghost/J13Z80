package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.ALU;
import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class INC extends ExecutableCommand {

	public INC(CPU core, Memory mem) {
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
		case ExecutableCommand.MR2MR:
			src = mem.read(core.get16(dest));
			src = ALU.add8((byte) src, (byte)1, core.registers);
			mem.write8(dest, src);
			ret = 7;
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IX:
			buf = core.get16(CPU.IXH)+core.memRead();
			buf &=0xffff;
			src = mem.read(buf);
			src = ALU.add8((byte) src, (byte)1, core.registers);
			mem.write8(dest, src);
			ret = 19;
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IY:
			buf = core.get16(CPU.IYH)+core.memRead();
			buf &=0xffff;
			src = mem.read(buf);
			src = ALU.add8((byte) src, (byte)1, core.registers);
			mem.write8(dest, src);
			ret = 19;
			break;
		case ExecutableCommand.RP2RP:
			src = (core.get16(dest)+1)&0xffff;
			core.registers[dest+1] = (byte)(src&0xff);
			core.registers[dest] = (byte)((src>>8)&0xff);
			ret = 2;
			break;
		case ExecutableCommand.RP2RP|ExecutableCommand.IX:
			src = (core.get16(CPU.IXH)+1)&0xffff;
			core.registers[CPU.IXL] = (byte)(src&0xff);
			core.registers[CPU.IXH] = (byte)((src>>8)&0xff);
			ret = 6;
			break;
		case ExecutableCommand.RP2RP|ExecutableCommand.IY:
			src = (core.get16(CPU.IYH)+1)&0xffff;
			core.registers[CPU.IYL] = (byte)(src&0xff);
			core.registers[CPU.IYH] = (byte)((src>>8)&0xff);
			ret = 6;
			break;
		case ExecutableCommand.R2R:
			src = core.registers[dest];
			src = ALU.add8((byte) src, (byte)1, core.registers);
			core.registers[dest]=(byte)src;
			break;
		}
		return ret;
	}

}
