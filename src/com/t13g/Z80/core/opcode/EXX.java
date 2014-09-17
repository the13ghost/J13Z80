package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class EXX extends ExecutableCommand {

	public EXX(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		swap(CPU.A,CPU.AA);
		swap(CPU.F,CPU.AF);
		swap(CPU.B,CPU.AB);
		swap(CPU.C,CPU.AC);
		swap(CPU.D,CPU.AD);
		swap(CPU.H,CPU.AH);
		swap(CPU.L,CPU.AL);
		swap(CPU.E,CPU.AE);
		return 0;
	}
	
	public void swap(int r, int r2) {
		core.registers[r] = (byte) (core.registers[r] ^ core.registers[r2]);
		core.registers[r2] = (byte) (core.registers[r] ^ core.registers[r2]);
		core.registers[r] = (byte) (core.registers[r] ^ core.registers[r2]);
		
	}

}
