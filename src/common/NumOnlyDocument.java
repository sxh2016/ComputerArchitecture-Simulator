package common;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class is used for the restriction of JTextField which can make the content in this field number only.
 *
 */
public class NumOnlyDocument extends PlainDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2322097990834185911L;

	@Override
	public void insertString(int offset, String s, AttributeSet attrSet)
			throws BadLocationException {
		try {
			//check if this string can be parse to a number
			//The reason to use long to parse is that the number value of the string may overflow the range of int
			Long.parseLong(s);
		} catch (NumberFormatException ex) {
			//ex.printStackTrace();
			return;
		}
		super.insertString(offset, s, attrSet);
	}
}
