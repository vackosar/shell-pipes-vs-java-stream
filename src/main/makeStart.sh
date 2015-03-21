#!/bin/sh
main () {
	cd "$(dirname "$0")/..";
	echo "Start number?";
	read no;
	{
		cat "src/main/template/startHead.sql"
		cat "in/ids$no.txt" | win2unix |
			while read ID; do
				cat "src/main/template/startBody.sql" |
					sed "s/\$ID/$ID/g";
			done
		cat "src/main/template/startTail.sql"
	} |
	  unix2win \
	  >"out/start$no.sql";
}
unix2win () {
	sed '/\r$/q;s/$/\r/';
}
win2unix () {
	sed 's/\r$//';
}
set -x -e;
main "$@";
