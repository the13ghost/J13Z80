package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;


public class IND extends ExecutableCommand {

	public IND(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int dest = CPU.C;
		byte port = core.registers[dest];
		
		core.registers[dest]=core.in8(port);

		mem.write8(core.get16(CPU.H), core.registers[dest]);
		
		int HL = core.get16(CPU.H);
		HL--;
		HL&=0xffff;
	
		core.registers[CPU.L] = (byte) (HL&255);
		core.registers[CPU.H]= (byte) (HL>>8&255);
		
		core.registers[CPU.B]--;
		
		core.registers[CPU.C] = port;
		
		core.registers[CPU.F] &= ~((1<<CPU.FLAG_Z)|(1<<CPU.FLAG_N));
		core.registers[CPU.F] |= core.registers[CPU.B]==0?1<<CPU.FLAG_Z:0;
		
		return 4;
	}

}
