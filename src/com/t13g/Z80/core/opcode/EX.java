package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class EX extends ExecutableCommand {

	public EX(CPU core, Memory mem) {
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

		// operate
		switch(op) {
		case ExecutableCommand.RP2RP:
			core.registers[src] = (byte) (core.registers[src] ^ core.registers[dest]);
			core.registers[dest] = (byte) (core.registers[src] ^ core.registers[dest]);
			core.registers[src] = (byte) (core.registers[src] ^ core.registers[dest]);
			
			core.registers[src+1] = (byte) (core.registers[src+1] ^ core.registers[dest+1]);
			core.registers[dest+1] = (byte) (core.registers[src+1] ^ core.registers[dest+1]);
			core.registers[src+1] = (byte) (core.registers[src+1] ^ core.registers[dest+1]);
			
			break;
		case ExecutableCommand.RP2MR:
			buf = mem.read(core.get16(dest));
			core.registers[src+1] = (byte) (core.registers[src+1] ^ buf);
			buf = core.registers[src+1] ^ buf;
			core.registers[src+1] = (byte) (core.registers[src+1] ^ buf);
			mem.write8(core.get16(dest), buf);
			buf = mem.read(core.get16(dest)+1);
			core.registers[src] = (byte) (core.registers[src] ^ buf);
			buf = core.registers[src] ^ buf;
			core.registers[src] = (byte) (core.registers[src] ^ buf);
			mem.write8(core.get16(dest)+1,buf);
			ret += 15;
			break;
		case ExecutableCommand.RP2MR|ExecutableCommand.IX:
			buf = mem.read(core.get16(dest));
			core.registers[CPU.IXL] = (byte) (core.registers[CPU.IXL] ^ buf);
			buf = core.registers[CPU.IXL] ^ buf;
			core.registers[CPU.IXL] = (byte) (core.registers[CPU.IXL] ^ buf);
			mem.write8(core.get16(dest), buf);
			buf = mem.read(core.get16(dest)+1);
			core.registers[CPU.IXH] = (byte) (core.registers[CPU.IXH] ^ buf);
			buf = core.registers[CPU.IXH] ^ buf;
			core.registers[CPU.IXH] = (byte) (core.registers[CPU.IXH] ^ buf);
			mem.write8(core.get16(dest)+1,buf);
			ret += 19;
			break;
		case ExecutableCommand.RP2MR|ExecutableCommand.IY:
			buf = mem.read(core.get16(dest));
			core.registers[CPU.IYL] = (byte) (core.registers[CPU.IYL] ^ buf);
			buf = core.registers[CPU.IYL] ^ buf;
			core.registers[CPU.IYL] = (byte) (core.registers[CPU.IYL] ^ buf);
			mem.write8(core.get16(dest), buf);
			buf = mem.read(core.get16(dest)+1);
			core.registers[CPU.IYH] = (byte) (core.registers[CPU.IYH] ^ buf);
			buf = core.registers[CPU.IYH] ^ buf;
			core.registers[CPU.IYH] = (byte) (core.registers[CPU.IYH] ^ buf);
			mem.write8(core.get16(dest)+1,buf);
			ret += 19;
			break;
			
		}
		return ret;
		}

	}
