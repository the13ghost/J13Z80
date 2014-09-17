package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class OUTI extends ExecutableCommand {

	public OUTI(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int dest = CPU.C;
		byte port = core.registers[dest];
		int src = core.get16(CPU.H);
		src = mem.read(src);
		
		core.out(port, (byte) src);
		
		
		int HL = core.get16(CPU.H);
		HL++;
		HL&=0xffff;
	
		core.registers[CPU.L] = (byte) (HL&255);
		core.registers[CPU.H]= (byte) (HL>>8&255);
		
		core.registers[CPU.B]--;
		
		return 4;
	}

}
