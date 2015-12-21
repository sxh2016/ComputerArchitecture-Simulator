package cpu.element;

import common.Configuration;
import common.Util;


/**
 * Define the basic unit in Memory
 *
 */
public class Word {
	
	/**
	 * The content in a word is an array of integers. Each integer in this array should be 1 or 0.
	 */
	private int[] content;
	
	/**
	 * get the content of this word
	 * @return int[] bits
	 */
	public int[] getContent() {
		return content;
	}

	/**
	 * if b.length >= 16
	 * 		keep the last 16 integers of b in this word;
	 * if b.length < 16
	 * 		use 0 to fill the empty position
	 * @param int[] b
	 */
	public void setContent(int[] b) {
		for(int i=0; i < Configuration.WORD_LENGTH; i++){
			this.content[Configuration.WORD_LENGTH - i - 1] = (i < b.length) ? (b[b.length - i - 1]) : 0;
		}
	}

	/**
	 * Default construction for Word
	 */
	public Word()
	{
		content = new int[Configuration.WORD_LENGTH];
	}
	
	/**
	 * Construct a word from an instance of Word
	 * @param Word w
	 */
	public Word(Word w)
	{
		content = w.getContent().clone();
	}
	
	/**
	 * Construct a word from an int
	 * @param Word w
	 */
	public Word(int data)
	{
		content = new int[Configuration.WORD_LENGTH];
		this.setContent(Util.getBinaryArrayFromIntValue(data, Configuration.WORD_LENGTH));
	}
	
	/**
	 * Construct a word from array
	 * @param int[] b
	 */
	public Word(int[] b)
	{
		content = new int[Configuration.WORD_LENGTH];
		this.setContent(b);
	}
	
	/**
	 * Return a copy of this word
	 * @return Word
	 */
	public Word clone(){
		return new Word(this);
	}

}
