package memory;

public class CacheConfiguration {

	/**
	 * how many cachelines in Cache
	 */
	public static final int CACHELINE_SIZE = 16;
	
	/**
	 * how many Words stored in each cachelines
	 */
	public static final int CACHELINE_CAPACITY = 8;
}
