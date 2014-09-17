package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;


public class CALL extends ExecutableCommand {

	public CALL(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		// get operation
		int op = options & 0xFFFF;
		// get source
		int src = options >> 16 & 0xFF;
		// get dest
		int dest = core.get16(CPU.SPH);
		// default return
		int ret = 6;

		switch(op){
		case ExecutableCommand.NONE:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			dest = (dest-1)&0xffff;
			mem.write8(dest, core.PC >> 8 &255);
			dest = (dest-1)&0xffff;
			mem.write8(dest, core.PC&255);
			core.PC=src;
			core.registers[CPU.SPL] = (byte)(dest & 0xff);
			core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
			ret += 5;
			break;
		case ExecutableCommand.Z:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_Z)&1)==1){
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC >> 8 &255);
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC&255);
				core.PC=src;
				core.registers[CPU.SPL] = (byte)(dest & 0xff);
				core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
				ret += 5;
			}
			break;
		case ExecutableCommand.NZ:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_Z)&1)==0){
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC >> 8 &255);
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC&255);
				core.PC = src;
				core.registers[CPU.SPL] = (byte)(dest & 0xff);
				core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
				ret += 5;
			}
			break;
		case ExecutableCommand.C:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_C)&1)==1){
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC >> 8 &255);
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC&255);
				core.PC=src;
				core.registers[CPU.SPL] = (byte)(dest & 0xff);
				core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
				ret += 5;
			}
			break;
		case ExecutableCommand.NC:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_C)&1)==0){
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC >> 8 &255);
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC&255);
				core.PC =src;
				core.registers[CPU.SPL] = (byte)(dest & 0xff);
				core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
				ret += 5;
			}
			break;
		case ExecutableCommand.M:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_S)&1)==1){
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC >> 8 &255);
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC&255);
				core.PC = src;
				core.registers[CPU.SPL] = (byte)(dest & 0xff);
				core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
				ret += 5;
			}
			break;
		case ExecutableCommand.P:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_S)&1)==0){
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC >> 8 &255);
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC&255);
				core.PC=src;
				core.registers[CPU.SPL] = (byte)(dest & 0xff);
				core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
				ret += 5;
			}
			break;
		case ExecutableCommand.PO:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_PV)&1)==0){
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC >> 8 &255);
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC&255);
				core.PC=src;
				core.registers[CPU.SPL] = (byte)(dest & 0xff);
				core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
				ret += 5;
			}
			break;
		case ExecutableCommand.PE:
			src = core.memRead()&255;
			src |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_PV)&1)==1){
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC >> 8 &255);
				dest = (dest-1)&0xffff;
				mem.write8(dest, core.PC&255);
				core.PC=src;
				core.registers[CPU.SPL] = (byte)(dest & 0xff);
				core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
				ret += 5;
			}
			break;
		}
		
		return ret;
	}

}
