package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class BITS extends ExecutableCommand {

	RLC rlc;
	RRC rrc;
	RL rl;
	RR rr;
	SLA sla;
	SRA sra;
	//SLL sll;
	SRL srl;
	BIT bit;
	RES res;
	SET set;
	public BITS(CPU core, Memory mem) {
		super(core, mem);
		rlc = new RLC(core, mem);
		rrc = new RRC(core, mem);
		rl = new RL(core, mem);
		rr = new RR(core, mem);
		sla = new SLA(core, mem);
		sra = new SRA(core, mem);
		srl = new SRL(core, mem);
		bit = new BIT(core, mem);
		res = new RES(core, mem);
		set = new SET(core, mem);
	}

	@Override
	public int execute(int options) {
		int op = core.memRead();
		if(op < 0x10){
			if((op&0xf)<0x08)
				rlc.execute((CPU.regList[op&0x7]<<16)|
							((op&0x7)==6?
									ExecutableCommand.MR2MR:
									ExecutableCommand.R2R)|options);
			else
				rrc.execute((CPU.regList[op&0x7]<<16)|
							((op&0x7)==6?
									ExecutableCommand.MR2MR:
									ExecutableCommand.R2R)|options);
		} else if(op<0x20){
			if((op&0xf)<0x08)
				rl.execute((CPU.regList[op&0x7]<<16)|
							((op&0x7)==6?
									ExecutableCommand.MR2MR:
										ExecutableCommand.R2R)|options);
			else
				rr.execute((CPU.regList[op&0x7]<<16)|
							((op&0x7)==6?
									ExecutableCommand.MR2MR:
									ExecutableCommand.R2R)|options);
		} else if(op<0x30){
			if((op&0xf)<0x08)
				sla.execute((CPU.regList[op&0x7]<<16)|
							((op&0x7)==6?
									ExecutableCommand.MR2MR:
										ExecutableCommand.R2R)|options);
			else
				sra.execute((CPU.regList[op&0x7]<<16)|
							((op&0x7)==6?
									ExecutableCommand.MR2MR:
									ExecutableCommand.R2R)|options);
		} else if(op<0x40){
			if((op&0xf)<0x08);
				//sll
			else
				srl.execute((CPU.regList[op&0x7]<<16)|
							((op&0x7)==6?
									ExecutableCommand.MR2MR:
									ExecutableCommand.R2R)|options);
		} else if(op < 0x80){
			bit.execute((((((op&0xf0)-0x40)>>3)+((op&0xf)>0x7?1:0))&0xff)<<24
						|(CPU.regList[op&0x7]<<16)|
						((op&0x7)==6?
								ExecutableCommand.MR2MR:
								ExecutableCommand.R2R)|options);
		} else if(op < 0xC0){
			res.execute((((((op&0xf0)-0x80)>>3)+((op&0xf)>0x7?1:0))&0xff)<<24
						|(CPU.regList[op&0x7]<<16)|
						((op&0x7)==6?
								ExecutableCommand.MR2MR:
								ExecutableCommand.R2R)|options);
		} else {
			set.execute((((((op&0xf0)-0xC0)>>3)+((op&0xf)>0x7?1:0))&0xff)<<24
						|(CPU.regList[op&0x7]<<16)|
						((op&0x7)==6?
								ExecutableCommand.MR2MR:
								ExecutableCommand.R2R)|options);
		}
		return 0;
	}

}
