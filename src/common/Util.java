package common;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * This class consists of commonly use functions
 *
 */
public class Util {
	
	/**
	 * format of hit rate percentage
	 */
	private static Format PERCENTAGE_FORMAT = new DecimalFormat("0.00%");
	
	/**
	 * Transfer int value to binary array
	 * @param value
	 * @param size
	 * @return int[]
	 */
	public static int[] getBinaryArrayFromIntValue(int value,int size){
		int[] data = new int[size];
		for(int i = 0;i < size; i++){
			data[size-1-i] = value%2;
			value = value/2;
		}
		return data;
	}
	
	/**
	 * Transfer binary array to int value
	 * @param int[] data
	 * @return
	 */
	public static int getIntValueFromBinaryArray(int[] data){
		int val = 0;
		for(int i = 0;i < data.length; i++){
			val = val*2 + data[i];
		}
		return val;
	}
	
	/**
	 * Transfer binary string to int value
	 * @param bs
	 * @return
	 * @throws NumberFormatException
	 */
	public static int getIntValueFromBinaryString(String bs) throws NumberFormatException{
		int val = 0;
		for(int i = 0;i < bs.length(); i++){
			val = val*2 + Integer.parseInt(String.valueOf(bs.charAt(i)));
		}
		return val;
	}
	
	/**
	 * Transfer binary array to Binary String
	 * @param bits
	 * @return
	 */
	public static String getBinaryStringFromBinaryArray(int[]bits){
		StringBuffer buf = new StringBuffer();
		for(int i=0;i<bits.length;i++){
			buf.append(bits[i]);
		}
		return buf.toString();
	}
	
	/**
	 * format double value into hit rate percentage
	 * @param value
	 * @return
	 */
	public static String formatPercentage(double value){
		return PERCENTAGE_FORMAT.format(value);
	}

}
