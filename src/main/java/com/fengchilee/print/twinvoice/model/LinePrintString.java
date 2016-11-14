/**
 * 
 */
package com.fengchilee.print.twinvoice.model;

import java.io.UnsupportedEncodingException;

import com.fengchilee.print.twinvoice.PrinterProtocol;

/**
 * 
 */
public class LinePrintString {
	
	private String leftStr = "";
	
	private String rightStr = "";

	private boolean leftDoubleWidth = false;
	
	private boolean rightDoubleWidth = false;

	private int fillWideSpace = 0;
	
	// 對齊
	private boolean justified = false;

	public LinePrintString(String str) {
		this.setLeftStr(str);
	}

	public LinePrintString(String leftStr, String rightStr) {
		this.setLeftStr(leftStr);
		this.setRightStr(rightStr);
	}

	public byte[] toBytes() throws UnsupportedEncodingException {

		return toPrinterString().getBytes(PrinterProtocol.TP_3688.Encoding);
	}

	public String toPrinterString() {

		return createOneLine(this.leftStr, this.leftDoubleWidth, this.rightStr,
					this.rightDoubleWidth, this.fillWideSpace);
	}

	private static String createOneLine(String leftStr, boolean left2Width, String rightStr,
			boolean right2Width, int fillWideSpace) {
	    
		StringBuffer sb = new StringBuffer();
		sb.append(PrinterProtocol.TP_3688.ESC_P);
		if (left2Width == true) {
			for (int j = 0; j < leftStr.length(); j++) {
				sb.append(PrinterProtocol.TP_3688.PRINT_WIDE);
				sb.append(leftStr.substring(j, j + 1));
			}
		} else {
			sb.append(leftStr);
		}

		sb.append(getFillString(leftStr, left2Width, rightStr, right2Width, fillWideSpace));

		if (right2Width == true) {
			for (int j = 0; j < rightStr.length(); j++) {
				sb.append(PrinterProtocol.TP_3688.PRINT_WIDE);
				sb.append(rightStr.substring(j, j + 1));
			}
		} else {
			sb.append(rightStr);
		}

		sb.append(PrinterProtocol.TP_3688.CR);

		return sb.toString();
	}
	
	private static String getFillString(String leftStr, boolean left2Width, String rightStr,
			boolean right2Width, int fillWideSpace) {

		int r = PrinterProtocol.MAX_LINE_LENGTH - getLength(leftStr, left2Width)
				- getLength(rightStr, right2Width);
		StringBuffer sb = new StringBuffer();
		if (r <= 0) {
			return sb.toString();
		}
		
		int reallyFill = fillWideSpace;
		
		if(fillWideSpace > 0) {
			
			r = r - fillWideSpace * 2;
			if (r <= 0) {
				reallyFill = (fillWideSpace * 2 + r) / 2;
			}
			
			for (int i = 0; i < reallyFill; i++) {
				sb.append(PrinterProtocol.TP_3688.PRINT_WIDE);
				sb.append(PrinterProtocol.SPACE);
			}
			
			while (r > 0) {
				sb.append(PrinterProtocol.SPACE);
				r--;
			}
		}
		return sb.toString();
	}

	public int length() {

		return getLength(leftStr, this.leftDoubleWidth) + getLength(rightStr, this.rightDoubleWidth);
	}

	public int totalWideChars() {
		int len = this.countTwChar(leftStr);

		if (this.leftDoubleWidth)
			len += this.leftStr.length();

		len += this.countTwChar(rightStr);

		if (this.rightDoubleWidth)
			len += this.rightStr.length();

		return len;
	}

	public int leftWideCharsCount() {
		return this.countTwChar(leftStr);
	}

	private int countTwChar(final String str) {
		int len = 0;
		for (int j = 0; j < str.length(); j++) {
			boolean twC = isTwChar(str.substring(j, j + 1));
			if (twC)
				len++;
		}

		return len;
	}

	private static boolean isTwChar(String s) {
		return (s.length() != s.getBytes().length);
	}

	private static String nullString(String str) {
		return (str == null ? "" : str);
	}

	private static int getLength(final String str, boolean wide) {
		int len = 0;
		for (int j = 0; j < str.length(); j++) {
			if (isTwChar(str.substring(j, j + 1))) {
				len += 2;
			} else {
				len++;
			}
		}

		return (wide == true ? len * 2 : len);
	}
	
	public boolean isJustified() {
		return justified;
	}

	public void setJustified(boolean justified) {
		this.justified = justified;
	}

	public String getLeftStr() {
		return leftStr;
	}

	public void setLeftStr(String leftStr) {
		this.leftStr = nullString(leftStr);
	}

	public void setLeftStr(String leftStr, boolean doubleWidth) {
		this.setLeftStr(leftStr);
		this.setLeftDoubleWidth(doubleWidth);
	}

	public String getRightStr() {
		return rightStr;
	}

	public void setRightStr(String rightStr) {
		this.rightStr = nullString(rightStr);
	}

	public void setRightStr(String rightStr, boolean doubleWidth) {
		this.setRightStr(rightStr);
		this.setRightDoubleWith(doubleWidth);
	}

	public boolean isLeftDoubleWidth() {
		return leftDoubleWidth;
	}

	public void setLeftDoubleWidth(boolean leftDoubleWidth) {
		this.leftDoubleWidth = leftDoubleWidth;
	}

	public boolean isRightDoubleWidth() {
		return rightDoubleWidth;
	}

	public void setRightDoubleWith(boolean rightDoubleWidth) {
		this.rightDoubleWidth = rightDoubleWidth;
	}

	public int getFillWideSpace() {
		return fillWideSpace;
	}

	public void setFillWideSpace(int fillWideSpace) {
		this.fillWideSpace = fillWideSpace;
	}
}
