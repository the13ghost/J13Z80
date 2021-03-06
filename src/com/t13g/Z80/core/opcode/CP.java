package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.ALU;
import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class CP extends ExecutableCommand {

	public CP(CPU core, Memory mem) {
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
		
		int buf =0;
		switch(op){
		case ExecutableCommand.I2R:
			src = core.memRead();
			ALU.sub8(core.registers[dest], 
					(byte) src, 
					core.registers);
			ret = 3;
			break;
		case ExecutableCommand.R2R:
			ALU.sub8(core.registers[dest],
					core.registers[src],
					core.registers);
			break;
		case ExecutableCommand.MR2R:
			src = mem.read(core.get16(src));
			ALU.sub8(core.registers[dest],
					(byte) src,
					core.registers);
			ret = 3;
			break;
		case ExecutableCommand.MR2R|ExecutableCommand.IX:
			buf =core.get16(CPU.IXH)+core.memRead();
			buf &=0xffff;
			src = mem.read(buf);
			ALU.sub8(core.registers[dest],
					(byte) src,
					core.registers);
			ret = 15;
			break;
		case ExecutableCommand.MR2R|ExecutableCommand.IY:
			buf = core.get16(CPU.IYH)+core.memRead();
			buf &= 0xffff;
			src = mem.read(buf);
			ALU.sub8(core.registers[dest],
					(byte) src,
					core.registers);
			ret = 15;
			break;
		}
		return ret;
	}

}
