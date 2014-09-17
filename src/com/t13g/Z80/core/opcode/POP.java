package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class POP extends ExecutableCommand {

	public POP (CPU cpu, Memory memory) {
		super(cpu, memory);
	}
	
	@Override
	public int execute(int options) {
		// get dest
		int dest = options >> 24 & 0xFF;
		// default return
		int ret = 6;
		// get src
		int src = core.get16(CPU.SPH);
		
		if ((options & ExecutableCommand.IX) == ExecutableCommand.IX){
			dest = CPU.IXH;
			ret += 4;
		}else if ((options & ExecutableCommand.IY) == ExecutableCommand.IY){
			dest = CPU.IYH;
			ret += 4;
		}
	
		
		core.registers[dest+1] = mem.read(src);
		src = (src+1)&0xffff;
		core.registers[dest] = mem.read(src);
		src = (src+1)&0xffff;
		
		core.registers[CPU.SPL] = (byte)(src & 0xff);
		core.registers[CPU.SPH] = (byte)(src >> 8 & 0xff);
		
		return ret;
	}

}
