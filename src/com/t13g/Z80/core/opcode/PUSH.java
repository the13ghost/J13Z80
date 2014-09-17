package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class PUSH extends ExecutableCommand {


	public PUSH (CPU cpu, Memory memory) {
		super(cpu,memory);
	}

	@Override
	public int execute(int options) {
		// get source
		int src = options >> 16 & 0xFF;
		// get dest
		int dest = core.get16(CPU.SPH);
		// default return
		int ret = 7;
		
		if ((options & ExecutableCommand.IX) == ExecutableCommand.IX){
			src = CPU.IXH;
			ret += 4;
		}else if ((options & ExecutableCommand.IY) == ExecutableCommand.IY){
			src = CPU.IYH;
			ret += 4;
		}
		
		dest = (dest-1)&0xffff;
		mem.write8(dest, core.registers[src]);
		dest = (dest-1)&0xffff;
		mem.write8(dest, core.registers[src+1]);
		
		core.registers[CPU.SPL] = (byte)(dest & 0xff);
		core.registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
		
		return ret;
	}

}
