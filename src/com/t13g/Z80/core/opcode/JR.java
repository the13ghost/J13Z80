package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class JR extends ExecutableCommand {

	public JR(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		// get operation
		int op = options & 0xFFFF;
		// get dest
		int dest;
		
		switch(op){
		case ExecutableCommand.NONE:
			core.PC += core.memRead();
			core.PC &= 0xffff;
			core.memRead();
			break;
		case ExecutableCommand.Z:
			if(((core.registers[CPU.F]>>CPU.FLAG_Z)&1)==1){
				core.PC += core.memRead();
				core.PC &= 0xffff;
				core.memRead();
			} else {
				core.memRead();
			}
			break;
		case ExecutableCommand.NZ:
			if(((core.registers[CPU.F]>>CPU.FLAG_Z)&1)==0){
				core.PC += core.memRead();
				core.PC &= 0xffff;
				core.memRead();
			} else {
				core.memRead();
			}
			break;
		case ExecutableCommand.C:
			if(((core.registers[CPU.F]>>CPU.FLAG_C)&1)==1){
				core.PC += core.memRead();
				core.PC &= 0xffff;
				core.memRead();
			} else {
				core.memRead();
			}
			break;
		case ExecutableCommand.NC:
			if(((core.registers[CPU.F]>>CPU.FLAG_C)&1)==0){
				core.PC += core.memRead();
				core.PC &= 0xffff;
				core.memRead();
			} else {
				core.memRead();
			}
			break;
		}
		
		
		return 0;
	}

}
