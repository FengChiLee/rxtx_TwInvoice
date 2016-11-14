/**
 * 
 */
package com.fengchilee.print.twinvoice;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fengchilee.print.twinvoice.PrinterInterface.PortInterface;
import com.fengchilee.print.twinvoice.printer.TwInvoiceSerialPrinter;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class TwInvoicePrinterFactory {

	private static final Logger log = LoggerFactory.getLogger(TwInvoicePrinterFactory.class);

	static OutputStream outputStream;

	static boolean outputBufferEmptyFlag = false;

	static ITwInvoicePrinter m_printer = null;

	/**
	 * 
	 */
	public TwInvoicePrinterFactory() {
	}

	/**
	 * 
	 * @param commInterface
	 * @return
	 * @throws NoSuchPortException
	 * @throws PortInUseException
	 * @throws TooManyListenersException
	 * @throws UnsupportedCommOperationException
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public static ITwInvoicePrinter createInvoicePrinter(PrinterInterface commInterface) throws NoSuchPortException,
			PortInUseException, TooManyListenersException, UnsupportedCommOperationException, IOException {

		if (m_printer != null && m_printer.isOpen()) {

			return m_printer;
		}

		m_printer = null;

		String portName = commInterface.getPortName();

		if (commInterface.getPortType() == PortInterface.Serial) { // CommPortIdentifier.PORT_SERIAL

			log.debug("Initialize serial port printer from {}", portName);

			TwInvoiceSerialPrinter.initialize(commInterface);

			m_printer = TwInvoiceSerialPrinter.getPrinter();

		} else {
			log.error("Unknow the port interface portType[{}], expect portTypes={}", commInterface.getPortType(),
					new Object[] { PortInterface.values() });

			throw new java.lang.UnsupportedOperationException();
		}

		return m_printer;
	}

}
