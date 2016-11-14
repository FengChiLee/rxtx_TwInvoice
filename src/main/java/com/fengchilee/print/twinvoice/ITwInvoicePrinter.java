/**
 * 
 */
package com.fengchilee.print.twinvoice;

import java.io.IOException;
import java.util.List;

import com.fengchilee.print.twinvoice.model.LinePrintString;

public interface ITwInvoicePrinter {

    boolean isOpen();
    
    void close();
    
    void print(final List<LinePrintString> printData) throws IOException;
    
}
