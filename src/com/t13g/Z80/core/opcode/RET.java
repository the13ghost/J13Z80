package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class RET extends ExecutableCommand {

	public RET(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int op = options & 0xFFFF;
		int src = core.get16(CPU.SPH);
		switch(op){
		case ExecutableCommand.NONE:
			core.PC = mem.read(src)&255;
			src = (src+1)&0xffff;
			core.PC |= (mem.read(src)&255)<<8;
			src = (src+1)&0xffff;
			break;
		case ExecutableCommand.Z:
			if(((core.registers[CPU.F]>>CPU.FLAG_Z)&1)==1){
				core.PC = mem.read(src)&255;
				src = (src+1)&0xffff;
				core.PC |= (mem.read(src)&255)<<8;
				src = (src+1)&0xffff;
			}
			break;
		case ExecutableCommand.NZ:
			if(((core.registers[CPU.F]>>CPU.FLAG_Z)&1)==0){
				core.PC = mem.read(src)&255;
				src = (src+1)&0xffff;
				core.PC |= (mem.read(src)&255)<<8;
				src = (src+1)&0xffff;
			}
			break;
		case ExecutableCommand.C:
			if(((core.registers[CPU.F]>>CPU.FLAG_C)&1)==1){
				core.PC = mem.read(src)&255;
				src = (src+1)&0xffff;
				core.PC |= (mem.read(src)&255)<<8;
				src = (src+1)&0xffff;
			}
			break;
		case ExecutableCommand.NC:
			if(((core.registers[CPU.F]>>CPU.FLAG_C)&1)==0){
				core.PC = mem.read(src)&255;
				src = (src+1)&0xffff;
				core.PC |= (mem.read(src)&255)<<8;
				src = (src+1)&0xffff;
			}
			break;
		case ExecutableCommand.M:
			if(((core.registers[CPU.F]>>CPU.FLAG_S)&1)==1){
				core.PC = mem.read(src)&255;
				src = (src+1)&0xffff;
				core.PC |= (mem.read(src)&255)<<8;
				src = (src+1)&0xffff;
			}
			break;
		case ExecutableCommand.P:
			if(((core.registers[CPU.F]>>CPU.FLAG_S)&1)==0){
				core.PC = mem.read(src)&255;
				src = (src+1)&0xffff;
				core.PC |= (mem.read(src)&255)<<8;
				src = (src+1)&0xffff;
			}
			break;
		case ExecutableCommand.PO:
			if(((core.registers[CPU.F]>>CPU.FLAG_PV)&1)==0){
				core.PC = mem.read(src)&255;
				src = (src+1)&0xffff;
				core.PC |= (mem.read(src)&255)<<8;
				src = (src+1)&0xffff;
			}
			break;
		case ExecutableCommand.PE:
			if(((core.registers[CPU.F]>>CPU.FLAG_PV)&1)==1){
				core.PC = mem.read(src)&255;
				src = (src+1)&0xffff;
				core.PC |= (mem.read(src)&255)<<8;
				src = (src+1)&0xffff;
			}
			break;
		}
		
		core.registers[CPU.SPL] = (byte)(src & 0xff);
		core.registers[CPU.SPH] = (byte)(src >> 8 & 0xff);
		
		
		return 0;
	}

}
