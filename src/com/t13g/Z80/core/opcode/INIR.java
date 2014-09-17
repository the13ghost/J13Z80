package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;


public class INIR extends ExecutableCommand {

	public INIR(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		while(core.registers[CPU.B]!=0){
			int dest = CPU.C;
			byte port = core.registers[dest];
			
			core.registers[dest] = core.in8(port);

			mem.write8(core.get16(CPU.H), core.registers[dest]);
		
			int HL = core.get16(CPU.H);
			HL++;
			HL&=0xffff;
	
			core.registers[CPU.L] = (byte) (HL&255);
			core.registers[CPU.H]= (byte) (HL>>8&255);
		
			core.registers[CPU.B]--;
			
			core.registers[CPU.C] = port;
		}
		
		return 4;
	}

}
