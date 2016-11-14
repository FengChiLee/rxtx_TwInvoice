package com.fengchilee.print.twinvoice.util;

import java.util.ArrayList;
import java.util.List;

import com.fengchilee.print.twinvoice.PrinterProtocol;
import com.fengchilee.print.twinvoice.model.LinePrintString;

public class LinePrintStringUtils {

	public static List<LinePrintString> create(String leftStr, String rightStr, boolean lineWrap) {
		return create(leftStr, false, rightStr, false, lineWrap);
	}
	
	public static List<LinePrintString> create(String leftStr, boolean left2Width, boolean lineWrap) {
		return create(leftStr, left2Width, "", false, lineWrap);
	}
	
	public static List<LinePrintString> create(String leftStr, boolean left2Width, String rightStr,
			boolean right2Width, boolean lineWrap) {

		String strL = leftStr;
		String strR = rightStr;
		
		List<LinePrintString> list = new ArrayList<LinePrintString>();				
		StringBuffer sb = new StringBuffer();
		int len = 0;
		for(;;) {
			len = cutString(strL, left2Width, len, sb);
			if(len <= PrinterProtocol.MAX_LINE_LENGTH) {
				strL = sb.toString();
				sb.delete(0, sb.length());
				break;
			} else {
				LinePrintString s = new LinePrintString(sb.toString());
				s.setLeftDoubleWidth(left2Width);
				list.add(s);
			}
			
			strL = strL.substring(sb.length());
			sb.delete(0, sb.length());
			len = 0;
		}
		
		for(;;) {
			len = cutString(strR, right2Width, len, sb);
			if(len <= PrinterProtocol.MAX_LINE_LENGTH) {
				LinePrintString s = new LinePrintString(strL, sb.toString());
				s.setLeftDoubleWidth(left2Width);
				s.setRightDoubleWith(right2Width);
				list.add(s);
				break;
			} else {
				LinePrintString s = new LinePrintString(strL, sb.toString());
				s.setLeftDoubleWidth(left2Width);
				s.setRightDoubleWith(right2Width);
				list.add(s);
			}
			
			strL = "";
			sb.delete(0, sb.length());
			len = 0;
		}
		
		if(!lineWrap) {
			return (list.size() > 0 ? list.subList(0, 1) : list);
		} else {
			return (list.size() > 2 ? list.subList(0, 2) : list);
		}
	}
	
	private static int cutString(String str, boolean doubleWidth, int startLen, StringBuffer sb) {
		int len = startLen;
		String tmp = "";
		for (int i = 0; i < str.length(); i++) {
			tmp = str.substring(i, i + 1);
			if(doubleWidth) {
				len += 2;
				len += (isTwChar(tmp) == true ? 2 : 0);
			} else {
				if(isTwChar(tmp)) {
					len += 2;
				} else {
					len++;
				}
			}
			
			if (len <= PrinterProtocol.MAX_LINE_LENGTH) {
				sb.append(tmp);
			} else {
				return len;
			}
		}
		
		return len;
	}
	
	private static boolean isTwChar(String s) {
		return (s.length() != s.getBytes().length);
	}
	
}
