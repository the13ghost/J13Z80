package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.ALU;
import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class ADD extends ExecutableCommand {

	
	public ADD (CPU cpu, Memory memory) {
		super(cpu, memory);
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
		int buf;
		switch(op){
		case ExecutableCommand.I2R:
			src = core.memRead();
			core.registers[dest] = ALU.add8(core.registers[dest], 
											(byte) src, 
											core.registers);
			ret = 3;
			break;
		case ExecutableCommand.R2R:
			core.registers[dest] = ALU.add8(core.registers[dest],
											core.registers[src],
											core.registers);
			break;
		case ExecutableCommand.MR2R:
			src = mem.read(core.get16(src));
			core.registers[dest] = ALU.add8(core.registers[dest],
											(byte) src,
											core.registers);
			ret = 3;
			break;
		case ExecutableCommand.MR2R|ExecutableCommand.IX:
			buf =core.get16(CPU.IXH)+core.memRead();
			buf&=0x0ffff;
			src = mem.read(buf);
			core.registers[dest] = ALU.add8(core.registers[dest],
											(byte) src,
											core.registers);
			ret = 15;
			break;
		case ExecutableCommand.MR2R|ExecutableCommand.IY:
			buf=core.get16(CPU.IYH)+core.memRead();
			buf&=0x0ffff;
			src = mem.read(buf);
			core.registers[dest] = ALU.add8(core.registers[dest],
											(byte) src,
											core.registers);
			ret = 15;
			break;
		case ExecutableCommand.RP2RP:
			src = core.get16(src);
			src = ALU.add16(src,
							core.get16(dest),
							core.registers);
			core.registers[dest+1] = (byte)(src&0xff);
			core.registers[dest] = (byte)((src>>8)&0xff);
			ret = 7;
			break;
		case ExecutableCommand.RP2RP|ExecutableCommand.IX:
			if(src == CPU.H) src = CPU.IXH;
			src = core.get16(src);
			src = ALU.add16(src,
							core.get16(CPU.IXH),
							core.registers);
			core.registers[CPU.IXL] = (byte)(src&0xff);
			core.registers[CPU.IXH] = (byte)((src>>8)&0xff);
			ret = 11;
			break;
		case ExecutableCommand.RP2RP|ExecutableCommand.IY:
			if(src == CPU.H) src = CPU.IYH;
			src = core.get16(src);
			src = ALU.add16(src,
							core.get16(CPU.IYH),
							core.registers);
			core.registers[CPU.IYL] = (byte)(src&0xff);
			core.registers[CPU.IYH] = (byte)((src>>8)&0xff);
			ret = 11;
			break;
		}
		
		return ret;
	}

}
