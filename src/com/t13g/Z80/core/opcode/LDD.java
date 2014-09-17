package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class LDD extends ExecutableCommand {

	public LDD(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int src = core.get16(CPU.H);
		int dest = core.get16(CPU.D);
		
		src = mem.read(src);
		mem.write8(dest, src);
		
		int HL = core.get16(CPU.H);
		HL--;
		HL&=0xffff;
		core.registers[CPU.L] = (byte) (HL&255);
		core.registers[CPU.H]= (byte) (HL>>8&255);
		
		int DE = core.get16(CPU.D);
		DE--;
		DE&=0xffff;
		core.registers[CPU.E] = (byte) (DE&255);
		core.registers[CPU.D]= (byte) (DE>>8&255);
		
		int BC = core.get16(CPU.B);
		BC--;
		BC&=0xffff;
		core.registers[CPU.C] = (byte) (BC&255);
		core.registers[CPU.B]= (byte) (BC>>8&255);
		
		core.registers[CPU.F]|=(BC==0xffff)?(1<<CPU.FLAG_PV):0;
		
		return 0;
	}

}
