package memory;

import memory.MainMemory;
import common.Util;
import exception.IllegalMemoryAddressException;
import cpu.element.Word;



/**
 * This class simulates the cache of a computer
 * 
 *
 */

public class Cache implements Memory{
	
	public static final int Record_NOT_UESED = -2;
	public static final int Record_JUST_NEW = -1;
	public static final int Record_IN_USE_SOME_TIME_AGO = 0;
	public static final int Record_IN_USE_JUST_NOW = 100;
	
	
	protected CacheLine[] lines;
	protected int LineToBeReplaced;
	protected CacheRecord record;
	
    private static Cache _instance;//
	
	public static Cache getInstance(){
		if(_instance==null)
			_instance = new Cache();
		return _instance;
	}
	
	public Cache(){
		record = new CacheRecord();
		lines = new CacheLine[CacheConfiguration.CACHELINE_SIZE];
		for(int i=0;i<CacheConfiguration.CACHELINE_SIZE;i++)
			lines[i] = new CacheLine();
	}
	
	
	/**
	 * search every valid cacheline's tag to find the hit data 
	 * return null where cache miss
	 * @param tag
	 * @param offset
	 * @return if hit then Word; else then null
	 */
	private Word hitCache(int tag, int offset) {
		for(int i=0;i<lines.length;i++){
			CacheLine line = lines[i];
			if(line.validflag&&line.tag==tag){
				record.tagCacheWordAsJustHit(i,offset);
				return line.data[offset];
			}
		}
		return null;
	}
	
	private Word loadCacheFromMemory(int tag,int offset) throws IllegalMemoryAddressException {
		//get the target cacheline
		CacheLine line = lines[LineToBeReplaced];
		
		//tag the replaced line in CachRecord
		record.tagCacheLineAsReplaced(LineToBeReplaced,offset);
		
		/*
		 * forward nextReplaced
		 */
		LineToBeReplaced++;
		if(LineToBeReplaced>=CacheConfiguration.CACHELINE_SIZE)
			LineToBeReplaced = 0;
		
		/*
		 * read block from memory
		 * Setting.CacheLine_Capacity words exactly
		 * start at tag*CACHELINE_CAPACITY
		 */
		int start = tag * CacheConfiguration.CACHELINE_CAPACITY;
		for(int i=0;i<CacheConfiguration.CACHELINE_CAPACITY;i++)
			//line.data[i].setValue(MainMemory.getInstance().read(start+i));////set value? word size is changed
		    line.data[i]=new Word(MainMemory.getInstance().read(start+i));
			line.tag = tag;
		line.validflag = true;
		
		//return target data
		return line.data[offset];
	}
	
	
	
	/*
	 * visited by CPU
	 * 
	 * @param index: physical index in memory
	 * @return
	 * @throws IllegalMemoryAddressException 
	 */
	public Word read(int index) throws IllegalMemoryAddressException{
		
		//calculate the Tag-value and offset
		int tag = index / CacheConfiguration.CACHELINE_CAPACITY;
		int offset = index % CacheConfiguration.CACHELINE_CAPACITY;
		
		//check each valid cacheline
		Word word = hitCache(tag,offset);
		
		//if cache-miss, load from memory
		if(word == null){
			word = loadCacheFromMemory(tag,offset);
			
		}else{
			
		}
		
		//return target data
		return word;
	}

	/**
	 * write-through
	 * 
	 * write allocate
	 * 
	 * @param index
	 * @param word
	 * @throws IllegalMemoryAddressException 
	 */
	
	public void write(int index, Word word) throws IllegalMemoryAddressException{
		int tag = index / CacheConfiguration.CACHELINE_CAPACITY;
		int offset = index % CacheConfiguration.CACHELINE_CAPACITY;
		Word oldWord = hitCache(tag,offset);
		if(oldWord == null){
			oldWord = loadCacheFromMemory(tag,offset);
		}
		oldWord.setContent(word.getContent());
		MainMemory.getInstance().write(index, word.getContent());
	}
	
	/**
	 * 
	 * @param index
	 * @param data
	 * @throws IllegalMemoryAddressException
	 */
	public void write(int index,int[] data) throws IllegalMemoryAddressException{
		Word word = new Word();
		word.setContent(data);
		write(index,word);
	}
	
	public Word getWordByLineAndOffset(int line,int offset){
		return lines[line].data[offset];
	}
	
	
	public int[][] getCacheRecord(){
		return record.records;
	}
	/**
	 * reset all information in Cache
	 * <br> nextReplaced will be set to 0
	 */
	public void clear(){
		record.initialize();
		for(CacheLine line:lines)
			line.validflag = false;
		LineToBeReplaced = 0;
	}
	
	/**
	 * get current hit rate information in String format
	 * @return
	 */
	public String getRateInfo(){
		String ret = "HitRate:";
		int sum = record.hit+record.miss;
		if(sum==0)
			ret+="N/A";
		else
			ret+=Util.formatPercentage(((double)record.hit)/sum);
		
		ret+=" (hit:"+record.hit+",miss:"+record.miss+")";
		
		return ret;
	}
	/**
	 * simulate cacheline which contains validflag, tag and data
	 * @author sunxiaohua
	 *
	 */
	class CacheLine{
		protected boolean validflag;
		protected int tag;
		protected Word[] data;
		
		public CacheLine(){
			tag = -1;
			validflag = false;
			data = new Word[CacheConfiguration.CACHELINE_CAPACITY];
			for(int i=0;i<CacheConfiguration.CACHELINE_CAPACITY;i++)
				data[i] = new Word();
		}
	}
	
	
	/**
	 * a class for recording cache information
	 * @author sunxiaohua
	 *
	 */
	
	class CacheRecord{
		
		private int records[][];
		private int hit;
		private int miss;
		
		public CacheRecord(){
			records = new int[CacheConfiguration.CACHELINE_SIZE][CacheConfiguration.CACHELINE_CAPACITY];//
			for(int i=0;i<CacheConfiguration.CACHELINE_SIZE;i++)
				records[i] = new int[CacheConfiguration.CACHELINE_CAPACITY];//
			initialize();
			
		}
		
		public void initialize(){
			for(int i=0;i<CacheConfiguration.CACHELINE_SIZE;i++)
				for(int j=0;j<CacheConfiguration.CACHELINE_CAPACITY;j++)
					records[i][j] = Record_NOT_UESED;
			hit = 0;
			miss = 0;
		}
		
		private void lowEveryWordRecord(){
			for(int i=0;i<CacheConfiguration.CACHELINE_SIZE;i++)
				for(int j=0;j<CacheConfiguration.CACHELINE_CAPACITY;j++)
					if(records[i][j]>Record_IN_USE_SOME_TIME_AGO)
						records[i][j]--;
		}
		
		private void tagCacheLineAsReplaced(int line, int offset){
			lowEveryWordRecord();
			for(int i=0;i<CacheConfiguration.CACHELINE_CAPACITY;i++)//
				records[line][i] = Record_JUST_NEW;
			records[line][offset] = Record_IN_USE_JUST_NOW;
			miss ++ ;
		}
		
		private void tagCacheWordAsJustHit(int line, int offset){
			lowEveryWordRecord();
			records[line][offset] = Record_IN_USE_JUST_NOW;
			hit ++;
		}
		
	}
	
}
