package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class RLD extends ExecutableCommand {

	public RLD(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		int src = core.get16(CPU.H);
		int nibble = mem.read(src)&0xf;
		int tnibble = (mem.read(src)&0xf0)>>8;
		int anibble = core.registers[CPU.A]&0xf;
		
		core.registers[CPU.A]=(byte) ((core.registers[CPU.A]&0xf0)|tnibble);
		mem.write8(src,nibble<<8|anibble);
		
		int parity = 0;
		for(int i=7;i>=0;i--)
			parity+=((core.registers[CPU.A]&(1<<i))>>i);
		core.registers[CPU.F] = (byte)(
				(core.registers[CPU.A]&0x80)|
				((core.registers[CPU.A]==0)?1<<CPU.FLAG_Z:0)|
				(((parity&1)==0)?1<<CPU.FLAG_PV:0));
		return 0;
	}

}
