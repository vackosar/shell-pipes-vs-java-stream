package com.vackosar.shellpipesvsjavastream;

import java.io.IOException;
import java.util.function.Function;

public class MakeStart extends UnixConsole {
	public static void main(String[] args) throws IOException {
		cd(getClassFileDir().resolve("../"));
		echo("Start number?");
		String no = readLine();
		concat(
			cat("src/main/template/startHead.sql"),
			cat("in/ids" + no + ".txt")
				.map(removeWindowsEOL())
				.flatMap(id ->
					cat("src/main/template/startBody.sql")
						.map(sed("\\$ID", id))
				),
			cat("src/main/template/startTail.sql")
		)
			.map(appendWindowsEOL())
			.collect(write("out/start" + no + ".sql"));
	}

	private static final String LINE_SEPARATOR_WIN_IN_UNIX = "\r";
	private static final String EMPTY = "";
	private static final String REGEX_LINE_END = "$";
	
	protected static Function<? super String, ? extends String> appendWindowsEOL() {
		return 
				removeWindowsEOL()
				.andThen(sed(REGEX_LINE_END, LINE_SEPARATOR_WIN_IN_UNIX));
	}

	protected static Function<? super String, ? extends String> removeWindowsEOL() {
		return sed(LINE_SEPARATOR_WIN_IN_UNIX + REGEX_LINE_END, EMPTY);
	}
}
