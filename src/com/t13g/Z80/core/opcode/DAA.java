package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class DAA extends ExecutableCommand {

	public DAA(CPU core, Memory mem) {
		super(core, mem);
	}

	@Override
	public int execute(int options) {
		byte sum = 0;
		int f = core.registers[CPU.F];
		byte acc = core.registers[CPU.A];
		boolean carry = (f&1)==1;
		boolean H = ((f&(1<<CPU.FLAG_H))>>CPU.FLAG_H)==1;
		boolean N = ((f&(1<<CPU.FLAG_N))>>CPU.FLAG_N)==1;
		
		if(carry)
			sum |= 0x60;
		else if(!H)
			sum |= 0x60;
		
		if(!carry&&!H&&(acc&0xf)>9)
			sum |= 0x06;
		if(!carry&&H&&(acc&0xf)<4)
			sum |= 0x06;
		if(carry&&!H&&(acc&0xf)>9)
			sum |= 0x06;
		if(carry&&H&&(acc&0xf)<4)
			sum |= 0x06;
		
		if(N)
			acc-=sum;
		else
			acc+=sum;
			
		if ((acc&255) > 0x99) {
			core.registers[CPU.F] |= 1;
		}
		
		if (acc == 0){
			core.registers[CPU.F] |= 1<<CPU.FLAG_Z;
		}
		
		int parity = 0;
		for(int i=7;i>=0;i--)
			parity+=((acc&(1<<i))>>i);
		
		core.registers[CPU.F] |= (parity&1)==0?1<<CPU.FLAG_PV:0;
		
		return 0;
	}

}
