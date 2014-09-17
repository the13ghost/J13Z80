package com.t13g.Z80.core;

import java.util.ArrayList;
import java.util.Scanner;

import com.t13g.Z80.ConsoleDebug;
import com.t13g.Z80.Ti83InterruptController;
import com.t13g.Z80.Ti83Memory;
import com.t13g.Z80.core.opcode.*;

/**
 * z80 microprocesor Emulator core
 * @author the13ghost
 *
 */
public class CPU {

	// timer
	private long clockTicks = 0;
	private long lastTime = 0;
	
	//16bit register
	public int PC;
	
	// rest of the registers
	public byte[] registers = new byte[24];
	
	// interrupt flip flops 
	public boolean IFF1 = false;
	public boolean IFF2 = false;
	
	public boolean IM0 = false;
	public boolean IM1 = false;
	public boolean IM2 = false;
	
	// halt state
	public boolean halted = false;
	
	//8bit
	public static final int A = 0;
	public static final int F = 1;
	public static final int B = 2;
	public static final int C = 3;
	public static final int D = 4;
	public static final int E = 5;
	public static final int H = 6;
	public static final int L = 7;
	
	// Alternate/shadow Registers
	public static final int AA = 8;
	public static final int AF = 9;
	public static final int AB = 10;
	public static final int AC = 11;
	public static final int AD = 12;
	public static final int AE = 13;
	public static final int AH = 14;
	public static final int AL = 15;
	
	
	//16bit
	//IX
	public static final int IXH = 16;
	public static final int IXL = 17;
	//IY
	public static final int IYH = 18;
	public static final int IYL = 19;
	//SP
	public static final int SPH = 20;
	public static final int SPL = 21;
	
	
	//8 bit
	public static final int I = 22;
	public static final int R = 23;
	
	//flag constants
	public static final int FLAG_S = 7;
	public static final int FLAG_Z = 6;
	public static final int FLAG_5 = 5;
	public static final int FLAG_H = 4;
	public static final int FLAG_3 = 3;
	public static final int FLAG_PV = 2;
	public static final int FLAG_N = 1;
	public static final int FLAG_C = 0;
	
	//clockspeed
	public static final int CLOCK = 6000000;
	public static final int VIDEO_CLOCK = CLOCK/60;
	
	// reglist for option generation
	public static final int[] regList = {B,C,D,E,H,L,0,A};
	
	//RAM
	private Memory memory;
	
	// devices connected to the CPU
	private ArrayList<PortDevice> portList=new ArrayList<PortDevice>();
	private InterruptController irqHandler;
	
	// lookup tables
	public ExecutableCommand[] opcodesTable;
	public int[] optionsTable;
	
	/**
	 * Constructor
	 * creates an instance of CPU with the defined memory
	 * @param mem
	 */
	public CPU(Memory mem){
		memory = mem;
		//registers[SPH]= (byte)0xF0;
		registers[SPH]= (byte)0xFF;
		registers[SPL]= (byte)0xFF;
		generateOptionsTable();
		generateOpcodeTable();
	}
	public boolean wasHalted;
	/**
	 * each step of the CPU is handled here
	 * @throws Exception
	 */
	public void tick() {
		clockTicks += 4;
		
		//if(PC == 56 )System.out.println("PC NOW :" +Integer.toHexString(PC));
		int opcode = memRead()&0xff;
		//if(PC == 56)System.out.println("Opcode NOW :" +Integer.toHexString(opcode));
		//if(PC == 56)(new Scanner(System.in)).nextLine();
		ExecutableCommand op = opcodesTable[opcode];
		int options = optionsTable[opcode];
		int ret = op.execute(options);
		clockTicks += ret;
		
		if (clockTicks - lastTime > CPU.CLOCK/200){
			int irq = irqHandler.check();
			if(irq != 0xffff) {
				//System.out.println("Interrupting " + Integer.toHexString(irq));
				//System.out.println(this);
				interrupt(irq);
			}
			lastTime = clockTicks;
		}
	}
	
	/**
	 * The Options table is created here,
	 * each operand has a specific option that contains the destination <p/>,
	 * the source and the operation that will be contained
	 */
	public void generateOptionsTable() { 
		optionsTable = new int[256];
		optionsTable[0x0] = 0; // nop
		optionsTable[0x1] = buildOption(B,0,ExecutableCommand.I2RP);
		optionsTable[0x2] = buildOption(B,A,ExecutableCommand.R2MR);
		optionsTable[0x3] = buildOption(B,0,ExecutableCommand.RP2RP);
		optionsTable[0x4] = buildOption(B,0,ExecutableCommand.R2R);
		optionsTable[0x5] = buildOption(B,0,ExecutableCommand.R2R);
		optionsTable[0x6] = buildOption(B,0,ExecutableCommand.I2R);
		optionsTable[0x7] = 0;// rlca
		optionsTable[0x8] = buildOption(A,AA,ExecutableCommand.RP2RP);
		optionsTable[0x9] = buildOption(H,B,ExecutableCommand.RP2RP);
		optionsTable[0xa] = buildOption(A,B,ExecutableCommand.MR2R);
		optionsTable[0xb] = buildOption(B,0,ExecutableCommand.RP2RP);
		optionsTable[0xc] = buildOption(C,0,ExecutableCommand.R2R);
		optionsTable[0xd] = buildOption(C,0,ExecutableCommand.R2R);
		optionsTable[0xe] = buildOption(C,0,ExecutableCommand.I2R);
		optionsTable[0xf] = 0; // rrca
		optionsTable[0x10] = 0;//djnz *
		optionsTable[0x11] = buildOption(D,0,ExecutableCommand.I2RP);
		optionsTable[0x12] = buildOption(D,A,ExecutableCommand.R2MR);
		optionsTable[0x13] = buildOption(D,0,ExecutableCommand.RP2RP);
		optionsTable[0x14] = buildOption(B,0,ExecutableCommand.R2R);
		optionsTable[0x15] = buildOption(D,0,ExecutableCommand.R2R);
		optionsTable[0x16] = buildOption(D,0,ExecutableCommand.I2R);
		optionsTable[0x17] = 0;//rla
		optionsTable[0x18] = 0;//jr *
		optionsTable[0x19] = buildOption(H,D,ExecutableCommand.RP2RP);
		optionsTable[0x1a] = buildOption(A,D,ExecutableCommand.MR2R);
		optionsTable[0x1b] = buildOption(D,0,ExecutableCommand.RP2RP);
		optionsTable[0x1c] = buildOption(C,0,ExecutableCommand.R2R);
		optionsTable[0x1d] = buildOption(C,0,ExecutableCommand.R2R);
		optionsTable[0x1e] = buildOption(E,0,ExecutableCommand.I2R);
		optionsTable[0x1f] = 0;//rra
		optionsTable[0x20] = ExecutableCommand.NZ;//jr nz, *
		optionsTable[0x21] = buildOption(H,0,ExecutableCommand.I2RP);
		optionsTable[0x22] = buildOption(0,H,ExecutableCommand.RP2MI);
		optionsTable[0x23] = buildOption(H,0,ExecutableCommand.RP2RP);
		optionsTable[0x24] = buildOption(H,0,ExecutableCommand.R2R);
		optionsTable[0x25] = buildOption(H,0,ExecutableCommand.R2R);
		optionsTable[0x26] = buildOption(H,0,ExecutableCommand.I2R);
		optionsTable[0x27] = 0;//daa
		optionsTable[0x28] = ExecutableCommand.Z;//jr z,*
		optionsTable[0x29] = buildOption(H,H,ExecutableCommand.RP2RP);
		optionsTable[0x2a] = buildOption(H,0,ExecutableCommand.MI2RP);
		optionsTable[0x2b] = buildOption(H,0,ExecutableCommand.RP2RP);
		optionsTable[0x2c] = buildOption(L,0,ExecutableCommand.R2R);
		optionsTable[0x2d] = buildOption(L,0,ExecutableCommand.R2R);
		optionsTable[0x2e] = buildOption(L,0,ExecutableCommand.I2R);
		optionsTable[0x2f] = 0;//cpl
		optionsTable[0x30] = ExecutableCommand.NC;//jr nc,*
		optionsTable[0x31] = buildOption(SPH,0,ExecutableCommand.I2RP);
		optionsTable[0x32] = buildOption(0,A,ExecutableCommand.R2MI);
		optionsTable[0x33] = buildOption(SPH,0,ExecutableCommand.RP2RP);
		optionsTable[0x34] = buildOption(H,0,ExecutableCommand.MR2MR);
		optionsTable[0x35] = buildOption(H,0,ExecutableCommand.MR2MR);
		optionsTable[0x36] = buildOption(H,0,ExecutableCommand.I2MR);
		optionsTable[0x37] = 0;//scf
		optionsTable[0x38] = ExecutableCommand.C;//jr c,*
		optionsTable[0x39] = buildOption(H,SPH,ExecutableCommand.RP2RP);
		optionsTable[0x3a] = buildOption(A,0,ExecutableCommand.MI2R);
		optionsTable[0x3b] = buildOption(SPH,0,ExecutableCommand.RP2RP);
		optionsTable[0x3c] = buildOption(A,0,ExecutableCommand.R2R);
		optionsTable[0x3d] = buildOption(A,0,ExecutableCommand.R2R);
		optionsTable[0x3e] = buildOption(A,0,ExecutableCommand.I2R);
		optionsTable[0x3f] = 0;//ccf
		for(int i=0x40;i<0x70;i++){
			int src = regList[i&0x7];
			int dest = regList[((i-0x40)>>3)|(((i&0xF)>=8)?1:0)];
			optionsTable[i] = buildOption(dest,src,ExecutableCommand.R2R);
		}
		for(int i=0x46;i<=0x6e;i+=8){
			int dest = regList[((i-0x40)>>3)|(((i&0xF)>=8)?1:0)];
			optionsTable[i] = buildOption(dest,H,ExecutableCommand.MR2R);
		}
		for(int i=0x70;i<=0x77;i++){
			int src = regList[i&0x7];
			optionsTable[i] = buildOption(H,src,ExecutableCommand.R2MR);
		}
		for(int i=0x78;i<0xC0;i++){
			int src = regList[i&0x7];
			optionsTable[i] = buildOption(A,src,ExecutableCommand.R2R);
		}
		optionsTable[0x76] = 0; // halt
		optionsTable[0x7e] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0x86] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0x8e] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0x96] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0x9e] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0xa6] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0xae] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0xb6] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0xbe] = buildOption(A,H,ExecutableCommand.MR2R);
		optionsTable[0xc0] = ExecutableCommand.NZ; //ret nz
		optionsTable[0xc1] = buildOption(B,0,0);
		optionsTable[0xc2] = ExecutableCommand.NZ; //jp nz,**
		optionsTable[0xc3] = ExecutableCommand.NONE; //jp **
		optionsTable[0xc4] = ExecutableCommand.NZ; //call nz, **
		optionsTable[0xc5] = buildOption(0,B,0);
		optionsTable[0xc6] = buildOption(A,0,ExecutableCommand.I2R);
		optionsTable[0xc7] = 0; //rst 00
		optionsTable[0xc8] = ExecutableCommand.Z; //ret z
		optionsTable[0xc9] = 0; //ret
		optionsTable[0xca] = ExecutableCommand.Z; //jp z,**
		optionsTable[0xcb] = 0; // bits
		optionsTable[0xcc] = ExecutableCommand.Z; //call z,**
		optionsTable[0xcd] = ExecutableCommand.NONE; //call **
		optionsTable[0xce] = buildOption(A,0,ExecutableCommand.I2R);
		optionsTable[0xcf] = 8; // rst 0x08
		optionsTable[0xd0] = ExecutableCommand.NC; //ret nc
		optionsTable[0xd1] = buildOption(D,0,0);
		optionsTable[0xd2] = ExecutableCommand.NC;//jp nc, **
		optionsTable[0xd3] = buildOption(0,A,ExecutableCommand.I2R);
		optionsTable[0xd4] = ExecutableCommand.NC; //call nc,**
		optionsTable[0xd5] = buildOption(0,D,0);
		optionsTable[0xd6] = buildOption(A,0,ExecutableCommand.I2R);
		optionsTable[0xd7] = 0x10; // rst 0x10
		optionsTable[0xd8] = ExecutableCommand.C; // ret c
		optionsTable[0xd9] = 0; // exx
		optionsTable[0xda] = ExecutableCommand.C; // jp c,**
		optionsTable[0xdb] = buildOption(A,0,ExecutableCommand.I2R); 
		optionsTable[0xdc] = ExecutableCommand.C; // call c, **
		optionsTable[0xdd] = 0; // IX
		optionsTable[0xde] = buildOption(A,0,ExecutableCommand.I2R);
		optionsTable[0xdf] = 0x18; // rst 0x18
		optionsTable[0xe0] = ExecutableCommand.PO; // ret po
		optionsTable[0xe1] = buildOption(H,0,0);
		optionsTable[0xe2] = ExecutableCommand.PO; // jp po, **
		optionsTable[0xe3] = buildOption(SPH,H,ExecutableCommand.RP2MR);  
		optionsTable[0xe4] = ExecutableCommand.PO; // call po,**
		optionsTable[0xe5] = buildOption(0,H,0);
		optionsTable[0xe6] = buildOption(A,0,ExecutableCommand.I2R);
		optionsTable[0xe7] = 0x20; // rst 0x20
		optionsTable[0xe8] = ExecutableCommand.PE; // ret pe
		optionsTable[0xe9] = ExecutableCommand.MR2MR; // jp (hl)
		optionsTable[0xea] = ExecutableCommand.PE; // jp pe,**
		optionsTable[0xeb] = buildOption(D,H,ExecutableCommand.RP2RP);
		optionsTable[0xec] = ExecutableCommand.PE; // call pe,**
		optionsTable[0xed] = 0; // extended
		optionsTable[0xee] = buildOption(A,0,ExecutableCommand.I2R); 
		optionsTable[0xef] = 0x28; // rest 0x28
		optionsTable[0xf0] = ExecutableCommand.P; // ret p
		optionsTable[0xf1] = buildOption(A,0,0);
		optionsTable[0xf2] = ExecutableCommand.P; // jp p,**
		optionsTable[0xf3] = 0; // di
		optionsTable[0xf4] = ExecutableCommand.P; // call p,**
		optionsTable[0xf5] = buildOption(0,A,0);
		optionsTable[0xf6] = buildOption(A,0,ExecutableCommand.I2R);
		optionsTable[0xf7] = 0x30; // rst 0x30
		optionsTable[0xf8] = ExecutableCommand.M; // ret m
		optionsTable[0xf9] = buildOption(SPH,H,ExecutableCommand.RP2RP);
		optionsTable[0xfa] = ExecutableCommand.M; // jp m, **
		optionsTable[0xfb] = 0; // ei
		optionsTable[0xfc] = ExecutableCommand.M; // call m,**
		optionsTable[0xfd] = 0; // IY
		optionsTable[0xfe] = buildOption(A,0,ExecutableCommand.I2R);
		optionsTable[0xff] = 0x38; // rst 0x38
	}
	
	/**
	 * 
	 * Builds the integer representation of the arguments 
	 * 32 bit int
	 * 8bits | 8bits | 16bits
	 * dest    src     operation
	 * 
	 * @param dest
	 * @param src
	 * @param op
	 * @return
	 */
	public int buildOption(int dest, int src, int op){
		return (dest<<24)|(src<<16)|(op);
	}
	
	/**
	 * Look up table for the opcodes
	 * a reference to an instance of an ExecutableCommand is stored in a 256-index array
	 */
	public void generateOpcodeTable() {
		NOP nop = new NOP(this, memory);
		LD ld = new LD(this,memory);
		POP pop = new POP(this,memory);
		PUSH push = new PUSH(this,memory);
		ADD add = new ADD(this,memory);
		ADC adc = new ADC(this,memory);
		AND and = new AND(this,memory);
		DEC dec = new DEC(this,memory);
		INC inc = new INC(this,memory);
		OR or = new OR(this,memory);
		RLCA rlca = new RLCA(this,memory);
		EX ex = new EX(this, memory);
		RRCA rrca = new RRCA(this,memory);
		DJNZ djnz = new DJNZ(this,memory);
		RLA rla = new RLA(this,memory);
		JR jr = new JR(this,memory);
		RRA rra = new RRA(this,memory);
		CPL cpl = new CPL(this,memory);
		DAA daa = new DAA(this,memory);
		CCF ccf = new CCF(this,memory);
		SUB sub = new SUB(this,memory);
		SBC sbc = new SBC(this,memory);
		HALT halt = new HALT(this,memory);
		XOR xor = new XOR(this,memory);
		CP cp = new CP(this,memory);
		RET ret = new RET(this,memory);
		JP jp = new JP(this,memory);
		CALL call = new CALL(this,memory);
		RST rst = new RST(this,memory);
		BITS bits = new BITS(this,memory);
		OUT out = new OUT(this,memory);
		IN in = new IN(this,memory);
		EXX exx = new EXX(this,memory);
		DI di = new DI(this,memory);
		EI ei = new EI(this,memory);
		EXTD extd = new EXTD(this,memory);
		IX ix = new IX(this,memory);
		IY iy = new IY(this,memory);
		
		opcodesTable = new ExecutableCommand[256];
		opcodesTable[0x0] = nop;
		for(int i=0x0; i<0x40;i+=0x10){
			opcodesTable[i+0x1]=ld;
			opcodesTable[i+0x2]=ld;
			opcodesTable[i+0x6]=ld;
			opcodesTable[i+0xa]=ld;
			opcodesTable[i+0xe]=ld;
		}
		for(int i=0x03;i<0x40;i+=0x10){
			opcodesTable[i]=inc;
			opcodesTable[i+1]=inc;
			opcodesTable[i+9]=inc;
		}
		for(int i=0x5;i<0x40;i+=0x10){
			opcodesTable[i]=dec;
			opcodesTable[i+6]=dec;
			opcodesTable[i+8]=dec;
		}
		opcodesTable[0x7] = rlca;
		opcodesTable[0x8] = ex;
		opcodesTable[0x9] = add;
		opcodesTable[0xf] = rrca;
		opcodesTable[0x10] = djnz;
		opcodesTable[0x17] = rla;
		opcodesTable[0x18] = jr;
		opcodesTable[0x19] = add;
		opcodesTable[0x1f] = rra;
		opcodesTable[0x20] = jr;
		opcodesTable[0x27] = rla;
		opcodesTable[0x28] = jr;
		opcodesTable[0x29] = add;
		opcodesTable[0x2f] = cpl;
		opcodesTable[0x30] = jr;
		opcodesTable[0x37] = daa;
		opcodesTable[0x38] = jr;
		opcodesTable[0x39] = add;
		opcodesTable[0x3f] = ccf;
		for(int i=0x40; i<0x80; i++){
			opcodesTable[i] = ld;
		}
		opcodesTable[0x76] = halt;
		for(int i=0x80;i<0x88;i++){
			opcodesTable[i] = add;
		}
		for(int i=0x88;i<0x90;i++){
			opcodesTable[i] = adc;
		}
		for(int i=0x90; i<0x98;i++){
			opcodesTable[i] = sub;
		}
		for(int i=0x98; i<0xa0;i++){
			opcodesTable[i] = sbc;
		}
		for(int i=0xa0;i<0xa8;i++){
			opcodesTable[i] = and;
		}
		for(int i=0xa8;i<0xb0;i++){
			opcodesTable[i] = xor;
		}
		for(int i=0xb0;i<0xb8;i++){
			opcodesTable[i] = or;
		}
		for(int i=0xb8;i<0xc0;i++){
			opcodesTable[i] = cp;
		}
		opcodesTable[0xc0] = ret;
		opcodesTable[0xc1] = pop;
		opcodesTable[0xc2] = jp;
		opcodesTable[0xc3] = jp;
		opcodesTable[0xc4] = call;
		opcodesTable[0xc5] = push;
		opcodesTable[0xc6] = add;
		opcodesTable[0xc7] = rst;
		opcodesTable[0xc8] = ret;
		opcodesTable[0xc9] = ret;
		opcodesTable[0xca] = jp;
		opcodesTable[0xcb] = bits;//BITS
		opcodesTable[0xcc] = call;
		opcodesTable[0xcd] = call;
		opcodesTable[0xce] = adc;
		opcodesTable[0xcf] = rst;
		opcodesTable[0xd0] = ret;
		opcodesTable[0xd1] = pop;
		opcodesTable[0xd2] = jp;
		opcodesTable[0xd3] = out;
		opcodesTable[0xd4] = call;
		opcodesTable[0xd5] = push;
		opcodesTable[0xd6] = sub;
		opcodesTable[0xd7] = rst;
		opcodesTable[0xd8] = ret;
		opcodesTable[0xd9] = exx;
		opcodesTable[0xda] = jp;
		opcodesTable[0xdb] = in;
		opcodesTable[0xdc] = call;
		opcodesTable[0xdd] = ix;//IX
		opcodesTable[0xde] = sbc;
		opcodesTable[0xdf] = rst;
		opcodesTable[0xe0] = ret;
		opcodesTable[0xe1] = pop;
		opcodesTable[0xe2] = jp;
		opcodesTable[0xe3] = ex;
		opcodesTable[0xe4] = call;
		opcodesTable[0xe5] = push;
		opcodesTable[0xe6] = and;
		opcodesTable[0xe7] = rst;
		opcodesTable[0xe8] = ret;
		opcodesTable[0xe9] = jp;
		opcodesTable[0xea] = jp;
		opcodesTable[0xeb] = ex;
		opcodesTable[0xec] = call;
		opcodesTable[0xed] = extd;//extd
		opcodesTable[0xee] = xor;
		opcodesTable[0xef] = rst;
		opcodesTable[0xf0] = ret;
		opcodesTable[0xf1] = pop;
		opcodesTable[0xf2] = jp;
		opcodesTable[0xf3] = di;
		opcodesTable[0xf4] = call;
		opcodesTable[0xf5] = push;
		opcodesTable[0xf6] = or;
		opcodesTable[0xf7] = rst;
		opcodesTable[0xf8] = ret;
		opcodesTable[0xf9] = ld;
		opcodesTable[0xfa] = jp;
		opcodesTable[0xfb] = ei;
		opcodesTable[0xfc] = call;
		opcodesTable[0xfd] = iy;//IY
		opcodesTable[0xfe] = cp;
		opcodesTable[0xff] = rst;
	}

	public byte memRead() {
		byte out = memory.read(PC);
		PC++;
		PC&=0xffff;
		return out;
		
	}
	public byte memPeek() {
		return (byte) (memory.read(PC)&0xff);
	}
	
	/*
	 * HARDWARE I/O
	 */
	public void addHardware(PortDevice ports) {
		portList.add(ports);
	}
	public byte in8(byte read) {
		for(int i=0; i<portList.size();i++){
			if(portList.get(i).containsPort(read&255)){
				return portList.get(i).read(read&355);
				
			}
		}
		return (byte) 0xff;
	}
	public void out(byte read, byte g) {
		for(int i=0; i<portList.size();i++){
			if(portList.get(i).containsPort(read&255)){
				portList.get(i).write(read&255,g);
				return;
			}
		}
	}
	
	
	/*
	 * Helper methods
	 */
	public int get16(int r) {
		int buf = (registers[r]&255)<<8;
		buf|=registers[r+1]&255;
		return buf;
	}
	
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("State of CPU\n");
		out.append("Ticks: "+clockTicks+"\n");
		out.append("PC "+Integer.toHexString(PC)+"\n");
		out.append("Registers:\n");
		out.append("A:"+registers[A] + " " + Integer.toHexString(registers[A]&0xff));
		out.append(" F:"+registers[F]+ " " + Integer.toHexString(registers[F]&0xff)+"\n");
		out.append("B:"+registers[B]+ " " + Integer.toHexString(registers[B]&0xff));
		out.append(" C:"+registers[C]+ " " + Integer.toHexString(registers[C]&0xff)+"\n");
		out.append("D:"+registers[D]+ " " + Integer.toHexString(registers[D]&0xff));
		out.append(" E:"+registers[E]+ " " + Integer.toHexString(registers[E]&0xff)+"\n");
		out.append("H:"+registers[H]+ " " + Integer.toHexString(registers[H]&0xff));
		out.append(" L:"+registers[L]+ " " + Integer.toHexString(registers[L]&0xff)+"\n");
		out.append("SP: " + Integer.toHexString(get16(SPH))+"\n");
		return out.toString();
	}
	
	
	/*
	 * TESTER
	 */
	public static void main(String[] args){
		System.out.println("Zilog Z80 emuv2 test _._>");
		System.out.println();
		Scanner input = new Scanner(System.in);
		System.out.println("Load a binary program(input in Hex): ");
		String program = input.nextLine();
		Ti83Memory mem = new Ti83Memory();
		mem.loadProgram(program);
		System.out.println("Program Loaded.");
		System.out.println("Running");
		CPU cpu = new CPU(mem);
		cpu.addHardware(new ConsoleDebug());
		for(;;){
			try {
				cpu.tick();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public long getClockTicks() {
		return clockTicks;
	}

	public void interrupt(int irq) {
		int dest = get16(CPU.SPH);
		// check if halted
		if(halted){
			//System.out.println("unhalted " + IM0 + " " + IM1 + " " +IM2);
			PC++;
			PC&=0xffff;
			halted = false;
			wasHalted = true;
		}
		
		if(!IFF1) return;
		
		dest = (dest-1)&0xffff;
		memory.write8(dest, (PC&0xff00)>>8);
		dest = (dest-1)&0xffff;
		memory.write8(dest, PC&0xff);
		

		registers[CPU.SPL] = (byte)(dest & 0xff);
		registers[CPU.SPH] = (byte)(dest >> 8 & 0xff);
		
		IFF1=false;
		IFF2=false;
		
		if(IM0||IM1){
			PC = 56;
			return;
		} else if(IM2){
			//System.out.println("IM2");
			irq = (irq&0xff)|(registers[I]<<8);
			PC = memory.read(irq)&0xff;
			irq++;
			irq&=0xffff;
			PC = ((memory.read(irq)&0xff)<<8 )| PC;
		}
		return;
	}

	public void addInterruptController(InterruptController intCont) {
		irqHandler = intCont;
	}
}