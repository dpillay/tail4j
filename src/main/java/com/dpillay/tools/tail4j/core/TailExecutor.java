package com.dpillay.tools.tail4j.core;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TailExecutor<T> {
	public void execute(List<TailedFileReader<T>> tailedFiles,
			TailPrinter<T> printer) {
		ExecutorService executor = Executors.newFixedThreadPool(tailedFiles
				.size() + 1);
		for (TailedFileReader<T> tailedFile : tailedFiles) {
			executor.submit(tailedFile);
		}
		executor.submit(printer);
	}
}
