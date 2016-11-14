/**
 * 
 */
package com.fengchilee.print.twinvoice.printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fengchilee.print.twinvoice.ITwInvoicePrinter;
import com.fengchilee.print.twinvoice.PrinterInterface;
import com.fengchilee.print.twinvoice.PrinterProtocol;
import com.fengchilee.print.twinvoice.model.LinePrintString;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class TwInvoiceSerialPrinter implements ITwInvoicePrinter, SerialPortEventListener {

	private final static Logger log = LoggerFactory.getLogger(TwInvoiceSerialPrinter.class);

	private static OutputStream outputStream;
	
	// private static boolean outputBufferEmptyFlag = false;

	private static TwInvoiceSerialPrinter m_printer = null;
	
	public static boolean initialize(PrinterInterface serialParams) 
			throws NoSuchPortException, PortInUseException, TooManyListenersException,
			UnsupportedCommOperationException, IOException {

		if (m_printer == null) {
			m_printer = new TwInvoiceSerialPrinter();
		}

		if (m_printer.isOpen()) {
			m_printer.close();
		}

		return m_printer.open(serialParams.getPortName(), serialParams.getSpeed(), 
				serialParams.getDatabits(), serialParams.getStopbits(), serialParams.getParity());
	}
	
	public static TwInvoiceSerialPrinter getPrinter() {
				
		return m_printer;
	}

	// ---------- instance variables

	private SerialPort serialPort;
	private InputStream inputStream;

	private boolean bOpened = false;

	public TwInvoiceSerialPrinter() {
	}
	
	public boolean open(final String portName, int speedBaudRate, int databits, int stopbits,
			int parity) throws NoSuchPortException, PortInUseException, TooManyListenersException,
			UnsupportedCommOperationException, IOException {

		boolean portFound = false;
		CommPortIdentifier portId = null;
		// parse ports and if the default port is found, initialize the reader
		@SuppressWarnings("rawtypes")
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(portName)) {
					
					log.debug("Found matched serial port {}", portName);
					
					portFound = true;
					break;
				}
			}
		} // while

		if (!portFound) {
			throw new NoSuchPortException();
		}

		if (portId.isCurrentlyOwned()) {
			log.debug("Current owned. The owner name is {}", portId.getCurrentOwner());
			
			return false;
		} else {
			// initialize serial port, application name, waiting time
			serialPort = (SerialPort) portId.open("Rxtx_write_TwInvoice", 2000);
			inputStream = serialPort.getInputStream();

			serialPort.addEventListener(this);
			// activate the DATA_AVAILABLE notifier
			serialPort.notifyOnDataAvailable(true);
			// set port parameters
			// serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
			// SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setSerialPortParams(speedBaudRate, databits, stopbits, parity);
			bOpened = true;
		}

		return true;
	}

	public void close() {

		this.bOpened = false;
		
		if (serialPort != null) {
			serialPort.close();
		}
	}

	public boolean isOpen() {
		return this.bOpened;
	}

	public void print(final List<LinePrintString> printData) throws IOException {
		if (printData == null || printData.size() == 0) {
			log.warn("Request printing data is empty, ignore it!!");
			return;
		}

		// handle paging
		paging(printData);
		
		//		
		write_port(printData);
	}
	
    // ----------
	
	// write_port() assumes that the port has already been opened
	private void write_port(List<LinePrintString> printData) throws IOException {
		
		log.debug("init write port....");
		
		// get the output stream
		outputStream = serialPort.getOutputStream();
		// activate the OUTPUT_BUFFER_EMPTY notifier
		serialPort.notifyOnOutputEmpty(true);

		for (LinePrintString str : printData) {
			outputStream.write(str.toBytes());
		}

		log.debug("write port ok.");
				
		try {
			outputStream.close();
		} catch(Exception e) {
			log.error("{}", e);
		}		
	}

	private void paging(List<LinePrintString> list) {

		boolean lastIsV = false;
		for (int i = 0; i < list.size(); i++) {
			lastIsV = false;
			if ((i + 1) % PrinterProtocol.MAX_LINES == 0) {
				LinePrintString line = new LinePrintString(PrinterProtocol.TP_3688.ESC_V);
				list.add(i + 1, line);
				lastIsV = true;
			}
		}

		if (!lastIsV) {
			LinePrintString line = new LinePrintString(PrinterProtocol.TP_3688.ESC_V);
			list.add(line);
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) {

		switch (event.getEventType()) {
		case SerialPortEvent.BI: // 10
		case SerialPortEvent.OE: // 7
		case SerialPortEvent.FE: // 9
		case SerialPortEvent.PE: // 8
		case SerialPortEvent.CD: // 6
		case SerialPortEvent.CTS: // 3
		case SerialPortEvent.DSR: // 4
		case SerialPortEvent.RI: // 5
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
			break;

		case SerialPortEvent.DATA_AVAILABLE: // 1
			// we get here if data has been received
			byte[] read_buffer = new byte[20];
			int numberBytes = 0;

			try {
				// read data
				while (inputStream.available() > 0) {
					numberBytes = inputStream.read(read_buffer);
				}

				// print data	
				log.debug("Read {} bytes, Data={}", numberBytes, new String(read_buffer));
				
			} catch (IOException e) {
			}

			break;
		default:
		} // switch
	}
	
}
