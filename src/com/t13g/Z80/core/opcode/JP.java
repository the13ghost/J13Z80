package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class JP extends ExecutableCommand {

	public JP(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		// get operation
		int op = options & 0xFFFF;
		int dest = core.PC;
		int buf;
		int ret = 6;
		switch(op){
		case ExecutableCommand.MR2MR:
			core.PC = core.get16(CPU.H);
			ret = 4;
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IX:
			core.PC = core.get16(CPU.IXH);
			ret = 4;
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IY:
			core.PC = core.get16(CPU.IYH);
			ret = 4;
			break;
		case ExecutableCommand.NONE:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			core.PC = buf;
			break;
		case ExecutableCommand.Z:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_Z)&1)==1){
				core.PC = buf;
			}
			break;
		case ExecutableCommand.NZ:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_Z)&1)==0){
				core.PC = buf;
			}
			break;
		case ExecutableCommand.C:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_C)&1)==1){
				core.PC = buf;
			}
			break;
		case ExecutableCommand.NC:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_C)&1)==0){
				core.PC = buf;
			}
			break;
		case ExecutableCommand.M:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_S)&1)==1){
				core.PC = buf;
			}
			break;
		case ExecutableCommand.P:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_S)&1)==0){
				core.PC = buf;
			}
			break;
		case ExecutableCommand.PO:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_PV)&1)==0){
				core.PC = buf;
			}
			break;
		case ExecutableCommand.PE:
			buf = core.memRead()&0xff;
			buf |= (core.memRead()&0xff)<<8;
			if(((core.registers[CPU.F]>>CPU.FLAG_PV)&1)==1){
				core.PC = buf;
			}
			break;
		}
		return ret;
	}
}
