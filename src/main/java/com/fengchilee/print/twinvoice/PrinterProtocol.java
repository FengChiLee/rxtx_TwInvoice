/**
 * 
 */
package com.fengchilee.print.twinvoice;

public interface PrinterProtocol {

	byte[] b_CR = new byte[] { 0x0D }; // 跳行
	byte[] b_TAB = new byte[] { 0x09 }; // tab
	byte[] b_SPACE = new byte[] { 0x20 }; // space

	final String TAB = new String(b_TAB);
	final String SPACE = new String(b_SPACE);

	final int MAX_LINES = 20;
	final int MAX_LINE_LENGTH = 24;

	final int FONT_WIDTH_en = 7; // Alphanumeric, 7x9
	final int FONT_WIDTH_zh_TW = 16; // Chinese, 16x9

	public interface TP_3688 {

		String Encoding = "Big5";

		// 控制的參數 byte陣列
		// B:0x42, R:0x52, J:0x49
		byte[] b_ESC_V_B = new byte[] { 0x1B, 0x1B, 0x56, 0x42 };
		byte[] b_ESC_V_R = new byte[] { 0x1B, 0x1B, 0x56, 0x52 };
		byte[] b_ESC_V_J = new byte[] { 0x1B, 0x1B, 0x56, 0x4A };
		
		/**
		 * 跳行列印<br />
		 * 為發票紙設計之複合指令,其同時完成動作有＜跳行＞＜列印＞<br />
		 * S = “R” 52h 收執聯列印<br />
		 * S = “J” 4Ah 存根聯列印<br />
		 * S = “B” 42h 收執聯及存根聯同步列印<br />
		 * S = “V” 56h 印證列印<br />
		 * m = “0” 30h 列印前不跳行<br />
		 * m = “1” 31h 列印前跳一行(印證列印時無意義)<br />
		 * d1 … dn 列印之資料<br />
		 * 列印倍寬 ASCII 字型: 0Eh ASCII<br />
		 * 列印倍寬中文字型: 以0Bh 來切換<br />
		 */
		byte[] b_ESC_P = new byte[] { 0x1B, 0x1B, 0x50, 0x42, 0x01 };
		// byte[] b_ESC_P_B = new byte[] { 0x1B, 0x1B, 0x50, 0x42, 0x01 };
		// byte[] b_ESC_P_R = new byte[] { 0x1B, 0x1B, 0x50, 0x52, 0x01 };
		// byte[] b_ESC_P_J = new byte[] { 0x1B, 0x1B, 0x50, 0x4A, 0x01 };
		// byte[] b_ESC_P_V = new byte[] { 0x1B, 0x1B, 0x50, 0x56, 0x01 };

		/** 送紙 */
		byte[] b_ESC_L = new byte[] { 0x1B, 0x1B, 0x4C }; // Line feed
		
		/** 蓋店名章 */
		byte[] b_ESC_S = new byte[] { 0x1B, 0x1B, 0x53 }; // Stamp
		
		/**
		 * 切紙<br />
		 * 此指令為立即切紙,不特定在發票背面之切紙標記點上
		 */
		byte[] b_ESC_C = new byte[] { 0x1B, 0x1B, 0x43 }; // Cut
		
		/**
		 * 驅動錢櫃 1
		 */
		byte[] b_ESC_G = new byte[] { 0x1B, 0x1B, 0x47 };
		
		/**
		 * 發票印表機初始化. (不清除接收暫存區資料)
		 */
		byte[] b_ESC_R = new byte[] { 0x1B, 0x1B, 0x52, 0x0D }; // Reset printer
		
		/**
		 * 詢問狀態(僅限使用串列通信RS232C 連線時使用)
		 */
		byte[] b_ESC_O = new byte[] { 0x1B, 0x1B, 0x4F };
		// ----------------------------------------------

		public final String PRINT_WIDE = new String(new byte[] { 0x0E });
		
		/**
		 * 發票送紙切紙<br />
		 */
		public final String ESC_V = new String(b_ESC_V_B);
		// public final String ESC_V_B = new String(b_ESC_V_B);
		// public final String ESC_V_R = new String(b_ESC_V_R);
		// public final String ESC_V_J = new String(b_ESC_V_J);
		
		/**
		 * 跳行列印<br />
		 */
		public final String ESC_P = new String(b_ESC_P);

		// public final String ESC_L = new String(b_ESC_L);

		public final String ESC_S = new String(b_ESC_S);

		public final String ESC_R = new String(b_ESC_R);

		public final String ESC_C = new String(b_ESC_C);
		
		/**
		 * 詢問狀態(僅限使用串列通信RS232C 連線時使用)
		 */
		public final String ESC_O = new String(b_ESC_O);
		
		/** 驅動錢櫃 1 */
		public final String ESC_G = new String(b_ESC_G);

		public final String CR = new String(b_CR);
		
	}

}
