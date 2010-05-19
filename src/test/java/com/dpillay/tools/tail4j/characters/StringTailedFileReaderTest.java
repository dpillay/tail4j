package com.dpillay.tools.tail4j.characters;

import java.io.File;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.dpillay.tools.tail4j.configuration.TailConfiguration;
import com.dpillay.tools.tail4j.core.PrintWriterTailPrinter;
import com.dpillay.tools.tail4j.core.TailExecutor;
import com.dpillay.tools.tail4j.core.TailListener;
import com.dpillay.tools.tail4j.core.TailPrinter;
import com.dpillay.tools.tail4j.core.TailedReader;

public class StringTailedFileReaderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testCall() {
		TailConfiguration tc = new TailConfiguration();
		tc.setFiles(Arrays.asList("/tail.file"));
		tc.setForce(false);
		tc.setShowLines(-1);

		PrintStream out = EasyMock.createMock(PrintStream.class);
		TailListener<String> tailListener = EasyMock
				.createMock(TailListener.class);

		List<TailedReader<String, File>> tailedFiles = new ArrayList<TailedReader<String, File>>();
		for (String filePath : tc.getFiles()) {
			File file = null;
			try {
				URI uri = StringTailedFileReader.class.getResource(filePath)
						.toURI();
				file = new File(uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			TailedReader<String, File> tailedFile = new StringTailedFileReader(
					tc, file, tailListener);
			tailedFiles.add(tailedFile);
		}
		TailPrinter<String> printer = new PrintWriterTailPrinter<String>(out,
				tailListener);

		// tailListener.onTail(EasyMock.isA(TailEvent.class));
		// EasyMock.expectLastCall();
		//
		// tailListener.onTail(EasyMock.isA(TailEvent.class));
		// EasyMock.expectLastCall();
		//
		// tailListener.onTail(EasyMock.isA(TailEvent.class));
		// EasyMock.expectLastCall();

		EasyMock.replay(out, tailListener);

		TailExecutor executor = new TailExecutor();
		executor.execute(tailedFiles, printer);

		// try {
		// Thread.sleep(50000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		EasyMock.verify(out, tailListener);
	}

}
