package com.dpillay.tools.tail4j.characters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;

import com.dpillay.tools.tail4j.configuration.TailConfiguration;
import com.dpillay.tools.tail4j.core.TailListener;
import com.dpillay.tools.tail4j.core.TailedReader;
import com.dpillay.tools.tail4j.exception.ApplicationException;
import com.dpillay.tools.tail4j.exception.ErrorCode;
import com.dpillay.tools.tail4j.model.TailEvent;

/**
 * Implements a tailed file reader for string based contents
 * 
 * @author dpillay
 */
public class StringTailedFileReader implements TailedReader<String, File> {
	private static char newLine = System.getProperty("line.separator")
			.charAt(0);

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
	public File getSource() {
		return file;
	}

	@Override
	public TailListener<String> getListener() {
		return listener;
	}

	@Override
	public void setSource(File file) {
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
			long showLineCount = this.configuration.getShowLines();
			if (showLineCount > 0) {
				long skipLinesLength = this.getSkipLinesLength(file,
						showLineCount);
				br.skip(skipLinesLength);
			} else {
				br.skip(file.length());
			}
			while (this.configuration.isForce() || showLineCount-- > 0) {
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

	private long getSkipLinesLength(File file, long showLineCount) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			long[] lineChars = new long[(int) showLineCount + 1];
			byte[] c = new byte[1024];
			long count = 0;
			int index = 0;
			int readChars = 0;
			long totalCharsRead = 0;
			while ((readChars = is.read(c)) != -1) {
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == newLine) {
						++count;
						if (index == lineChars.length)
							index = 0;
						lineChars[index++] = totalCharsRead + i + 1;
					}
				}
				totalCharsRead += readChars;
			}
			if (count >= showLineCount) {
				return lineChars[index];
			}
		} catch (Exception e) {
		}
		return 0;
	}
}
