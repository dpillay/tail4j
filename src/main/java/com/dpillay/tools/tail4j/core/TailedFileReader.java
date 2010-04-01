package com.dpillay.tools.tail4j.core;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Tails a file
 * 
 * @author dpillay
 * 
 * @param <T>
 */
public interface TailedFileReader<T> extends Callable<T> {

	/**
	 * Get the file being tailed
	 * 
	 * @return
	 */
	public File getFile();

	/**
	 * Set the file to be tailed
	 * 
	 * @param file
	 */
	public void setFile(File file);

	/**
	 * Get the tailed listener
	 * 
	 * @return
	 */
	public TailListener<T> getListener();

	/**
	 * Set the tailed listener
	 * 
	 * @param listener
	 */
	public void setListener(TailListener<T> listener);

}
