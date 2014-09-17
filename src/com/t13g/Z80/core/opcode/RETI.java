package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class RETI extends ExecutableCommand {

	public RETI(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int op = options & 0xFFFF;
		int src = core.get16(CPU.SPH);
		
		core.PC = mem.read(src)&255;
		src = (src+1)&0xffff;
		core.PC |= (mem.read(src)&255)<<8;
		src = (src+1)&0xffff;
		
		
		core.registers[CPU.SPL] = (byte)(src & 0xff);
		core.registers[CPU.SPH] = (byte)(src >> 8 & 0xff);
		
		if(core.IFF2) core.IFF1 =true;
		else core.IFF1 = false;
		
		
 		return 0;
	}

}
