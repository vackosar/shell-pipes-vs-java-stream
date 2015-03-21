package com.vackosar.shellvsjavastream;

import java.io.IOException;

public class MakeStart extends UnixConsole {
	public static void main(String[] args) throws IOException {
		cd(getClassFileDir().resolve("../"));
		echo("Start number?");
		String no = read();
		FileWriterConsumer write = new FileWriterConsumer("out/start" + no + ".sql");
		concat(
			cat("src/main/template/startHead.sql"),
			cat("in/ids" + no + ".txt")
				.map(MakeStart::removeWindowsEOL)
				.flatMap(id ->
					cat("src/main/template/startBody.sql")
						.map(sed("\\$ID", id))
				),
			cat("src/main/template/startTail.sql")
		)
			.map(MakeStart::appendWindowsEOL)
			.forEach(write);
		write.close();
	}

	private static final String LINE_SEPARATOR_WIN = "\r\n";
	private static final String LINE_SEPARATOR_WIN_IN_UNIX = "\r";
	private static final String EMPTY = "";
	private static final String REGEX_LINE_END = "$";
	
	protected static String appendWindowsEOL(String x) {
		if (x.matches(LINE_SEPARATOR_WIN + REGEX_LINE_END)) {
			return x;
		} else {
			return x + LINE_SEPARATOR_WIN;
		} 
	}

	protected static String removeWindowsEOL(String x) {
		return x.replaceAll(LINE_SEPARATOR_WIN_IN_UNIX + REGEX_LINE_END, EMPTY);
	}
}
