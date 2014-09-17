package com.t13g.Z80.core.opcode;

import com.t13g.Z80.core.CPU;
import com.t13g.Z80.core.Memory;

public class EXTD extends ExecutableCommand {

	IN in;
	OUT out;
	LDI ldi;
	LDIR ldir;
	CPI cpi;
	CPIR cpir;
	INI ini;
	INIR inir;
	SBC sbc;
	LD ld;
	OUTI outi;
	OTIR otir;
	NEG neg;
	RETN retn;
	IM im;
	RRD rrd;
	RLD rld;
	LDD ldd;
	LDDR lddr;
	CPD cpd;
	CPDR cpdr;
	IND ind;
	INDR indr;
	OUTD outd;
	OTDR otdr;
	ADC adc;
	RETI reti;
	
	public EXTD(CPU core, Memory mem) {
		super(core, mem);
		in = new IN(core,mem);
		out = new OUT(core,mem);
		ldi = new LDI(core,mem);
		ldir = new LDIR(core,mem);
		cpi = new CPI(core,mem);
		cpir = new CPIR(core,mem);
		ini = new INI(core,mem);
		inir = new INIR(core,mem);
		sbc = new SBC(core,mem);
		ld = new LD(core,mem);
		outi = new OUTI(core,mem);
		otir = new OTIR(core,mem);
		neg = new NEG(core,mem);
		retn = new RETN(core,mem);
		im = new IM(core,mem);
		rrd = new RRD(core,mem);
		rld = new RLD(core,mem);
		ldd = new LDD(core,mem);
		lddr = new LDDR(core,mem);
		cpd = new CPD(core,mem);
		cpdr = new CPDR(core,mem);
		ind = new IND(core,mem);
		indr = new INDR(core,mem);
		outd = new OUTD(core,mem);
		otdr = new OTDR(core,mem);
		adc = new ADC(core,mem);
		reti = new RETI(core,mem);
		
	}

	@Override
	public int execute(int options) {
		int[] regList = {CPU.B,CPU.D,CPU.H,CPU.C,CPU.E,CPU.L,CPU.A};
		byte op = core.memRead();
		if((op&0xf)==0&&op<(byte) 0x70){
			//in
			System.out.println((((op&0xf0)>>4)-4) + " this value "+ Integer.toHexString(op));
			in.execute(core.buildOption(regList[((op&0xf0)>>4)-4], CPU.C, ExecutableCommand.R2R));
		}else if((op&0xf)==1&&op<(byte) 0x80){
			//out
			out.execute(core.buildOption(CPU.C, regList[((op&0xf0)>>4)-4], ExecutableCommand.R2R));
		} else if((op&0xf)==2&&op<(byte) 0x80){
			//sbc
			sbc.execute(core.buildOption(CPU.H, regList[((op&0xf0)>>4)-4], 0));
		} else if((op&0xf)==3&&op<(byte) 0x80){
			//ld
			ld.execute(core.buildOption(0,regList[((op&0xf0)>>4)-4],ExecutableCommand.RP2MI));
		} else if((op&0xf)==4&&op<(byte) 0x80){
			//neg
			neg.execute(0);
		} else if((op&0xf)==5&&op<(byte) 0x80){
			//retn
			retn.execute(0);
		} else if((op&0xf)==6&&op<(byte) 0x80){
			//im
			im.execute(op&1);
		} else if((op&0xf)==7&&op<(byte) 0x60){
			//ld
			switch(op){
			case (byte) 0x47: ld.execute(core.buildOption(CPU.I,CPU.A,ExecutableCommand.R2R));
			break;
			case (byte) 0x57: ld.execute(core.buildOption(CPU.A,CPU.I,ExecutableCommand.R2R));
			break;
			}
		} else if((op&0xf)==7&&op>(byte) 0x60){
			//rrd
			rrd.execute(0);
		} else if((op&0xf)==8&&op<(byte) 0x80){
			//in
			System.out.println((((op&0xf0)>>4)-1) + " this value " + Integer.toHexString(op));
			in.execute(core.buildOption(regList[((op&0xf0)>>4)-1], CPU.C, ExecutableCommand.R2R));
		} else if((op&0xf)==9&&op<(byte) 0x80){
			// out
			out.execute(core.buildOption(CPU.C, regList[((op&0xf0)>>4)-1], ExecutableCommand.R2R));
		} else if((op&0xf)==0xa&&op<(byte) 0x80){
			// adc
			adc.execute(core.buildOption(CPU.H, regList[((op&0xf0)>>4)-4], 0));
		} else if((op&0xf)==0xb&&op<(byte) 0x80){
			// ld
			ld.execute(core.buildOption(regList[((op&0xf0)>>4)-4],0,ExecutableCommand.MI2RP));
		} else if((op&0xf)==0xd&&op==(byte) 0x4D){
			// reti
			reti.execute(0);
		} else if((op&0xf)==0xd&&op>(byte) 0x4D){
			// retn
			retn.execute(0);
		} else if((op&0xf)==0xe&&op<(byte) 0x80){
			// im
			im.execute(op&1);
		} else if((op&0xf)==0xf&&op<(byte) 0x60){
			// ld
			if(op==(byte) 0x4F)
				ld.execute(core.buildOption(CPU.R,CPU.A,ExecutableCommand.R2R));
			else
				ld.execute(core.buildOption(CPU.A,CPU.R,ExecutableCommand.R2R));
		} else if((op&0xf)==0xf&&op>(byte) 0x60){
			// rld
			rld.execute(0);
		}
		switch(op){
		case (byte) 0xA0: ldi.execute(0);break;//ldi
		case (byte) 0xB0: ldir.execute(0);break;//ldir
		case (byte) 0xA1: cpi.execute(0);break;//cpi
		case (byte) 0xB1: cpir.execute(0);break;//cpir
		case (byte) 0xA2: ini.execute(0);break;//ini
		case (byte) 0xB2: inir.execute(0);break;//inir
		case (byte) 0xA3: outi.execute(0);break;//outi
		case (byte) 0xB3: otir.execute(0);break;//otir
		case (byte) 0xA8: ldd.execute(0);break;//ldd
		case (byte) 0xB8: lddr.execute(0);break;//lddr
		case (byte) 0xA9: cpd.execute(0);break;//cpd
		case (byte) 0xB9: cpdr.execute(0);break;//cpdr
		case (byte) 0xAA: ind.execute(0);break;//ind
		case (byte) 0xBA: indr.execute(0);break;//indr
		case (byte) 0xAB: outd.execute(0);break;//outd
		case (byte) 0xBB: otdr.execute(0);break;//otdr
		}
			
		return 0;
	}

}
