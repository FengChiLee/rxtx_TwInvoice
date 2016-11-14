package com.fengchilee.print.twinvoice;

import java.io.IOException;
import java.util.TooManyListenersException;

import org.junit.Test;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class TwInvoicePrinterFactoryTest {

	@Test(expected = UnsupportedOperationException.class)
	public void testEmptyInterface() throws NoSuchPortException, PortInUseException, TooManyListenersException,
			UnsupportedCommOperationException, IOException {

		TwInvoicePrinterFactory.createInvoicePrinter(new PrinterInterface());
	}


	
}
