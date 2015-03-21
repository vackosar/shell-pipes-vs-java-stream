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
}
