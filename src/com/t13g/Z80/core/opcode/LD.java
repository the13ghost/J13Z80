package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class LD extends ExecutableCommand {

	public LD (CPU cpu, Memory memory) {
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
		
		// operate
		switch(op) {
		case ExecutableCommand.RP2RP:
			core.registers[dest+1]=core.registers[src+1];
			core.registers[dest] = core.registers[src];
			ret += 2;
			break;
		case ExecutableCommand.RP2RP|ExecutableCommand.IX:
			core.registers[dest+1]=core.registers[CPU.IXL];
			core.registers[dest] = core.registers[CPU.IXH];
			ret += 6;
			break;
		case ExecutableCommand.RP2RP|ExecutableCommand.IY:
			core.registers[dest+1]=core.registers[CPU.IYL];
			core.registers[dest] = core.registers[CPU.IYH];
			ret += 6;
			break;
		case ExecutableCommand.R2MR:
			src  = core.registers[src];
			dest = core.get16(dest);
			mem.write8(dest,src);
			ret += 3;
			break;
		case ExecutableCommand.R2MR|ExecutableCommand.IX:
			src  = core.registers[src];
			dest = core.get16(CPU.IXH)+core.memRead();
			dest &= 0xffff;
			mem.write8(dest,src);
			ret += 15;
			break;
		case ExecutableCommand.R2MR|ExecutableCommand.IY:
			src  = core.registers[src];
			dest = core.get16(CPU.IYH)+core.memRead();
			dest &= 0xffff;
			mem.write8(dest,src);
			ret += 15;
			break;
		case ExecutableCommand.I2MR:
			src  = core.memRead();
			dest = core.get16(dest);
			mem.write8(dest, src);
			ret += 6;
			break;
		case ExecutableCommand.I2MR|ExecutableCommand.IX:
			src  = core.memRead();
			dest = core.get16(CPU.IXH)+src;
			dest &= 0xffff;
			mem.write8(dest, src);
			ret += 15;
			break;
		case ExecutableCommand.I2MR|ExecutableCommand.IY:
			src  = core.memRead();
			dest = core.get16(CPU.IYH)+src;
			dest &= 0xffff;
			mem.write8(dest, src);
			ret += 15;
			break;
		case ExecutableCommand.R2MI:
			src  = core.registers[src];
			dest = core.memRead()&255;
			dest |=(core.memRead()&0xff)<<8;
			mem.write8(dest, src);
			ret += 9;
			break;
		case ExecutableCommand.RP2MI:
			dest = core.memRead()&255;
			dest |=(core.memRead()&0xff)<<8;
			mem.write16(dest, 
						core.registers[src], 
						core.registers[src+1]);
			ret += 12;
			break;
		case ExecutableCommand.RP2MI|ExecutableCommand.IX:
			dest = core.memRead()&255;
			dest |=(core.memRead()&0xff)<<8;
			mem.write16(dest, 
						core.registers[CPU.IXH], 
						core.registers[CPU.IXL]);
			ret += 16;
			break;
		case ExecutableCommand.RP2MI|ExecutableCommand.IY:
			dest = core.memRead()&255;
			dest |=(core.memRead()&0xff)<<8;
			mem.write16(dest, 
						core.registers[CPU.IYH], 
						core.registers[CPU.IYL]);
			ret += 16;
			break;
		case ExecutableCommand.MR2R:
			core.registers[dest] = mem.read(core.get16(src));
			ret += 3;
			break;
		case ExecutableCommand.MR2R|ExecutableCommand.IX:
			src = core.get16(CPU.IXH)+core.memRead();
			src &= 0xffff;
			core.registers[dest] = mem.read(src);
			ret += 15;
			break;
		case ExecutableCommand.MR2R|ExecutableCommand.IY:
			src = core.get16(CPU.IYH)+core.memRead();
			src &= 0xffff;
			core.registers[dest] = mem.read(src);
			ret += 15;
			break;
		case ExecutableCommand.MI2R:
			src = core.memRead()&255;
			src |=(core.memRead()&0xff)<<8;
			core.registers[dest] = mem.read(src);
			ret += 9;
			break;
		case ExecutableCommand.R2R:
			core.registers[dest] = core.registers[src];
			break;
		case ExecutableCommand.MI2RP|ExecutableCommand.IX:
			src = core.memRead()&255;
			src |=(core.memRead()&0xff)<<8;
			core.registers[CPU.IXL] = mem.read(src);
			core.registers[CPU.IXH] = mem.read(src+1);
			ret += 16;
			break;
		case ExecutableCommand.MI2RP|ExecutableCommand.IY:
			src = core.memRead()&255;
			src |=(core.memRead()&0xff)<<8;
			core.registers[CPU.IYL] = mem.read(src);
			core.registers[CPU.IYH] = mem.read(src+1);
			ret += 16;
			break;
		case ExecutableCommand.MI2RP:
			src = core.memRead()&255;
			src |=(core.memRead()&0xff)<<8;
			core.registers[dest+1] = mem.read(src);
			core.registers[dest] = mem.read(src+1);
			ret += 12;
			break;
		case ExecutableCommand.I2RP:
			core.registers[dest+1] = core.memRead();
			core.registers[dest] = core.memRead();
			ret += 6;
			break;
		case ExecutableCommand.I2RP|ExecutableCommand.IY:
			core.registers[CPU.IYL] = core.memRead();
			core.registers[CPU.IYH] = core.memRead();
			ret += 10;
			break;
		case ExecutableCommand.I2RP|ExecutableCommand.IX:
			core.registers[CPU.IXL] = core.memRead();
			core.registers[CPU.IXH] = core.memRead();
			ret += 10;
			break;
		case ExecutableCommand.I2R:
			core.registers[dest] = core.memRead();
			ret += 3;
			break;
		default:
			System.out.println("Broken Option " + op + ": " + Integer.toHexString(op));
		}
		return ret;
	}
}
