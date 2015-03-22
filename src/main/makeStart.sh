#!/bin/sh
main () {
	cd "$(dirname "$0")/..";
	echo "Start number?";
	read no;
	{
		cat "src/main/template/startHead.sql";
		cat "in/ids$no.txt" |
			removeWindowsEOL |
			while read id; do
				cat "src/main/template/startBody.sql" |
					sed "s/\$ID/$id/g";
			done
		cat "src/main/template/startTail.sql";
	} |
	appendWindowsEOL \
	  >"out/start$no.sql";
}
appendWindowsEOL () {
	removeWindowsEOL |
	sed 's/$/\r/';
}
removeWindowsEOL () {
	sed 's/\r$//';
}
set -x -e;
main "$@";
