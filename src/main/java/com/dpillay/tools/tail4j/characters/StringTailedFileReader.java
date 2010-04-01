package com.dpillay.tools.tail4j.characters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.dpillay.tools.tail4j.configuration.TailConfiguration;
import com.dpillay.tools.tail4j.core.TailListener;
import com.dpillay.tools.tail4j.core.TailedFileReader;
import com.dpillay.tools.tail4j.exception.ApplicationException;
import com.dpillay.tools.tail4j.exception.ErrorCode;
import com.dpillay.tools.tail4j.model.TailEvent;

/**
 * Implements a tailed file reader for string based contents
 * 
 * @author dpillay
 */
public class StringTailedFileReader implements TailedFileReader<String> {
	private File file = null;
	private TailListener<String> listener = null;
	private TailConfiguration configuration = null;

	public StringTailedFileReader(TailConfiguration tc, File file,
			TailListener<String> listener) {
		super();
		this.file = file;
		this.listener = listener;
		this.configuration = tc;
	}

	public StringTailedFileReader() {
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public TailListener<String> getListener() {
		return listener;
	}

	@Override
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public void setListener(TailListener<String> listener) {
		this.listener = listener;
	}

	public TailConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(TailConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public String call() throws Exception {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			long skipLines = this.configuration.getSkipLines();
			br.skip(file.length());
			while (this.configuration.isForce() || skipLines-- > 0) {
				String line = br.readLine();
				if (line == null) {
					Thread.sleep(200);
					continue;
				}
				TailEvent<String> event = TailEvent.generateEvent(line, line
						.length());
				this.listener.onTail(event);
			}
		} catch (Throwable t) {
			throw new ApplicationException(t, ErrorCode.DEFAULT_ERROR,
					"Could not finish tailing file");
		}
		return null;
	}
}
