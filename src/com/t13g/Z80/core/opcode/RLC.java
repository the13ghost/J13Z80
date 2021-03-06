package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class RLC extends ExecutableCommand {

	public RLC(CPU core, Memory mem) {
		super(core, mem);
		
	}

	@Override
	public int execute(int options) {
		int op = options&0xffff;
		int src = options>>>16& 0xFF;
		int dest = options>>>16& 0xFF;
		int c=0;
		switch(op){
		case ExecutableCommand.MR2MR:
			dest = core.get16(CPU.H);
			src = mem.read(dest);
			c = (src&(1<<7))>>7;
			src <<= 1;
			src |= c;
			mem.write8(dest, src);
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IX:
			dest = core.get16(CPU.IXH)+core.memRead();
			dest &= 0xffff;
			src = mem.read(dest);
			c = (src&(1<<7))>>7;
			src <<= 1;
			src |= c;
			mem.write8(dest, src);
			break;
		case ExecutableCommand.MR2MR|ExecutableCommand.IY:
			dest = core.get16(CPU.IYH)+core.memRead();
			dest &= 0xffff;
			src = mem.read(dest);
			c = (src&(1<<7))>>7;
			src <<= 1;
			src |= c;
			mem.write8(dest, src);
			break;
		case ExecutableCommand.R2R:
			c = (core.registers[src]&(1<<7))>>7;
			core.registers[src]<<=1;
			core.registers[src]|=c;
			break;
		}
		int parity =0;
		for(int i=7;i>=0;i--)
			parity+=((src&(1<<i))>>i);
		core.registers[CPU.F]=(byte)(((src<0)?1<<CPU.FLAG_S:0)|
									(src==0?1<<CPU.FLAG_Z:0)|
									((parity&1)==0?1<<CPU.FLAG_PV:0)|c);
		return 0;
	}

}
