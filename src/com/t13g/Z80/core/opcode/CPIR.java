package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.ALU;
import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class CPIR extends ExecutableCommand {

	public CPIR(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int src = core.get16(CPU.H);
		src = mem.read(src);
		byte dest = core.registers[CPU.A];

		while ( core.get16(CPU.B) != 0 && 
				(((core.registers[CPU.F]&(1<<CPU.FLAG_Z))>>CPU.FLAG_Z) == 1)){
					ALU.cpd8(dest, (byte) src, core.registers);

					src = core.get16(CPU.H);
					src++;
					src &=0xffff;

					core.registers[CPU.L] = (byte) (src&255);
					core.registers[CPU.H]= (byte) (src>>8&255);

					src = core.get16(CPU.B);
					src--;
					src &=0xffff;

					core.registers[CPU.F] &= ~(1<<CPU.FLAG_PV);
					core.registers[CPU.F] |= src!=0?1<<CPU.FLAG_PV:0;

					core.registers[CPU.C] = (byte) (src&255);
					core.registers[CPU.B]= (byte) (src>>8&255);
				}
		return 12;
	}

}
