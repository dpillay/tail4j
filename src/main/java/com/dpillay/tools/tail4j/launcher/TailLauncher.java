package com.dpillay.tools.tail4j.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.dpillay.tools.tail4j.characters.StringTailedFileReader;
import com.dpillay.tools.tail4j.configuration.TailConfiguration;
import com.dpillay.tools.tail4j.core.PrintWriterTailPrinter;
import com.dpillay.tools.tail4j.core.TailExecutor;
import com.dpillay.tools.tail4j.core.TailListener;
import com.dpillay.tools.tail4j.core.TailPrinter;
import com.dpillay.tools.tail4j.core.TailedFileReader;

public class TailLauncher {
	public static void main(String[] args) {
		TailConfiguration tc = TailLauncher.build(args);
		List<TailedFileReader<String>> tailedFiles = new ArrayList<TailedFileReader<String>>();
		TailListener<String> tailListener = new TailListener<String>();
		for (String filePath : tc.getFiles()) {
			File file = new File(filePath);
			TailedFileReader<String> tailedFile = new StringTailedFileReader(
					tc, file, tailListener);
			tailedFiles.add(tailedFile);
		}
		TailPrinter<String> printer = new PrintWriterTailPrinter<String>(
				System.out, tailListener);
		TailExecutor<String> executor = new TailExecutor<String>();
		executor.execute(tailedFiles, printer);
	}
	
	public static TailConfiguration build(String[] args) {
		TailConfiguration tc = new TailConfiguration();
		for (int i = 0; i < args.length; ++i) {
			String arg = args[i];
			if (arg.equals("-n")) {
				long showLines = -1;
				try {
					showLines = Long.parseLong(args[++i]);
				} catch (Throwable t) {
				}
				tc.setShowLines(showLines);
			} else if (arg.equals("-f")) {
				tc.setForce(true);
			} else {
				tc.getFiles().add(arg);
			}
		}
		return tc;
	}
}
