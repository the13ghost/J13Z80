package com.t13g.Z80.launcher;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.t13g.Z80.Ti83InterruptController;
import com.t13g.Z80.Ti83Keypad;
import com.t13g.Z80.Ti83LCD;
import com.t13g.Z80.Ti83Memory;
import com.t13g.Z80.Ti83MemoryController;
import com.t13g.Z80.core.CPU;

public class Ti83 extends Canvas implements Runnable{

	public static final int WIDTH = 96;
	public static final int HEIGHT = 64;
	public static final int SCALE = 8;
	
	private boolean running = true;
	
	public  CPU core;
	private Ti83Memory mem;
	private Ti83LCD screen;
	private Ti83Keypad keypad;
	private Ti83InterruptController intCont;
	private Ti83MemoryController memCont;
	
	public Ti83 () {
		
		mem = new Ti83Memory();
		core = new CPU(mem);
		
		screen = new Ti83LCD();
		keypad = new Ti83Keypad();
		intCont = new Ti83InterruptController();
		
		core.addHardware(screen);
		core.addHardware(keypad);
		//core.addHardware(TMS);
		core.addHardware(intCont);
		
		core.addInterruptController(intCont);
		intCont.addInterrupter(keypad);
		
		this.addKeyListener(keypad);
		
		//mem.loadProgram("3E01D3102115007EA7CA1200D31123C30700C31200183C1100");
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Ti83 Emulator");
		Ti83 calc = new Ti83();
		Dimension dim = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		
		calc.setMinimumSize(dim);
		calc.setMaximumSize(dim);
		calc.setPreferredSize(dim);
		
		frame.add(calc,BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		calc.start();
	}
	
	private void start() {
		new Thread(this).start();
	}
	
	public void init() {
		// check for existance of TI83.ROM file
		File romFile = new File("./TI83.ROM");
		if (romFile.exists()){
			//place Rom into MEM.ROM[]
			JOptionPane.showMessageDialog(null, "TI83.ROM was found\nReading to virtual ROM");
			
			try {
				FileInputStream input = new FileInputStream(romFile);
				byte[] tmp = new byte[262144];
				input.read(tmp);
				
				mem.loadRom(tmp);
				
				memCont = new Ti83MemoryController(mem);
				core.addHardware(memCont);
				
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "TI83.ROM was not Found.");
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "TI83.ROM could not be read.");
				e.printStackTrace();
			} 
			
			
			JOptionPane.showMessageDialog(null, "TI83.ROM has been copied.");
			
			
		} else {
			running = false;
			JOptionPane.showMessageDialog(null, "TI83.ROM was not found in the current directory.");
		}
	}

	@Override
	public void run() {
		
		long lastTime = 0;
		int frames = 0;
		int ticks = 0;
		long lastFPS = System.currentTimeMillis();

		init();
		
		while(running) {
			
			core.tick();			
			
			if(core.getClockTicks() - lastTime > CPU.VIDEO_CLOCK){
				frames++;
				lastTime+=CPU.VIDEO_CLOCK;
				//System.out.println(core);
				
				render();
				
				//core.interrupt(0xff);
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(System.currentTimeMillis() - lastFPS > 1000) {
				lastFPS += 1000;
				//System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
		
		System.exit(0);
	}

	private void render() {
		BufferStrategy strategy = getBufferStrategy();
		if (strategy == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}
		
		Graphics g = strategy.getDrawGraphics();
		
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, getHeight(), getWidth());
		
		g.drawImage(screen.getBuffer(), 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
		
		strategy.show();
	}

}
