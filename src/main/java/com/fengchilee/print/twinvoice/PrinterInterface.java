/**
 * 
 */
package com.fengchilee.print.twinvoice;

import java.util.Arrays;

import gnu.io.SerialPort;

public class PrinterInterface {

	public enum PortInterface {
		
		Serial	
	}	
	
	public static final int[] SPEED_SET = new int[] { 75, 110, 300, 1200, 2400, 4800, 9600, 19200,
			38400, 57600, 115200 };

	/**
	 * 1 = PORT_SERIAL<br />
	 * 2 = PORT_PARALLEL<br />
	 * 3 = PORT_SOCKET_RAW<br />
	 */
	private PortInterface portType = null;
	
	/** 並列/串列界面 連線參數 - Parallel / Serial port name / Socket IP & Port */
	private String portName = "";
	
	// 串列界面 連線參數
    // [格式]:Serial port name, 傳輸速率 speed(baud rate), parity, databits, stopbits

	/** 串列界面 連線參數 - 傳輸速率 speed(baud rate) */
	private int speed = 9600;

	/** 串列界面 連線參數 - parity */
	private int parity = SerialPort.PARITY_NONE;

	/** 串列界面 連線參數 - databits */
	private int databits = SerialPort.DATABITS_8;

	/** 串列界面 連線參數 - stopbits */
	private int stopbits = SerialPort.STOPBITS_1;
	
	private boolean useUsb2RS232Converter = false;
	
	public PrinterInterface() {
	}

	public PrinterInterface(PortInterface portType, String portName) {
		this.portType = portType;
		this.portName = portName;

		if (Arrays.binarySearch(PortInterface.values(), portType) >= 0) {
			
		} else {
			throw new java.lang.IllegalArgumentException("portType must be one of " + PortInterface.values());
		}
	}
	
	// -----------
	
	/**
     * @return the portType
     */
    public PortInterface getPortType() {
        return portType;
    }

    /**
     * @param portType the portType to set
     */
    public void setPortType(PortInterface portType) {
        this.portType = portType;
    }

    /**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}

	/**
	 * @param portName
	 *            the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return the parity
	 */
	public int getParity() {
		return parity;
	}

	/**
	 * @param parity
	 *            the parity to set
	 */
	public void setParity(int parity) {
		this.parity = parity;
	}

	/**
	 * @return the databits
	 */
	public int getDatabits() {
		return databits;
	}

	/**
	 * @param databits
	 *            the databits to set
	 */
	public void setDatabits(int databits) {
		this.databits = databits;
	}

	/**
	 * @return the stopbits
	 */
	public int getStopbits() {
		return stopbits;
	}

	/**
	 * @param stopbits
	 *            the stopbits to set
	 */
	public void setStopbits(int stopbits) {
		this.stopbits = stopbits;
	}
	
    /**
	 * @return the useUsb2Rs232Converter
	 */
	public boolean isUseUsb2Rs232Converter() {
		return useUsb2RS232Converter;
	}

	/**
	 * @param useUsb2Rs232Converter the useUsb2Rs232Converter to set
	 */
	public void setUseUsb2Rs232Converter(boolean useUsb2Rs232Converter) {
		this.useUsb2RS232Converter = useUsb2Rs232Converter;
	}

}
