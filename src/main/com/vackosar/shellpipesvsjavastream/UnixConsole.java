package com.vackosar.shellpipesvsjavastream;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class UnixConsole {
	private static URI workingDirectory = getInitialWorkingDirectory();
	protected static final String LINE_SEPARATOR_UNIX = "\n";
	
	static {
		// FIXME implementing cat command using scanner
		System.setProperty("line.separator", LINE_SEPARATOR_UNIX);
	}

	protected static URI getClassFileDir() {
		try {
			return MakeStart.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	protected static Function<? super String, ? extends String> sed(String regex, String replacement) {
		return line -> line.replaceAll(regex, replacement);
	}
	
	private static URI getInitialWorkingDirectory() {
		try {
			return new URI("file://" + System.getProperty("user.dir").replaceAll("\\" + System.getProperty("file.separator"), "/"));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	protected static void cd(URI uri) {
		workingDirectory = uri;
	}

	private static URI pwd() {
		return workingDirectory;
	}

	protected static class FileWriterCollector implements Collector<String, FileWriter, Void> {
			final String relativePath;
			public FileWriterCollector(String relativePath) {
				this.relativePath = relativePath;
			}

			@Override
			public BiConsumer<FileWriter, String> accumulator() {
				return new BiConsumer<FileWriter, String>() {
					@Override
					public void accept(FileWriter writer, String line) {
						try {
							writer.write(line);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				};
			}

			@Override
			public Set<java.util.stream.Collector.Characteristics> characteristics() {
				return Collections.emptySet();
			}

			@Override
			public BinaryOperator<FileWriter> combiner() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Function<FileWriter, Void> finisher() {
				return writer -> { 
					try {
						writer.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					} return null; 
				} ;
			}

			@Override
			public Supplier<FileWriter> supplier() {
				return new Supplier<FileWriter>() {
					@Override
					public FileWriter get() {
						File file = new File(pwd().resolve(relativePath));
						try {
							return new FileWriter(file);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				};
			}
		}

	protected static Stream<String> cat(String relativePath) {
		try {
			return Files.lines(Paths.get(pwd().resolve(relativePath)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SafeVarargs
	protected static <T> Stream<T> concat(Stream<T> ... streams) {
		return Stream.of(streams).reduce(Stream.empty(), Stream::concat);
	}

	protected static String read() {
		return System.console().readLine();
	}

	protected static void echo(String line) {
		System.console().printf(line + System.lineSeparator());
	}

	public UnixConsole() {
		super();
	}

}