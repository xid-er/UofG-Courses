#! /bin/bash

testCommand(){
	which $1 > dev/null 2>&1
	if [ "$?" -ne "0" ]; then
		echo "ERROR: $1 not found"
		exit $2
	fi
}

if [ $# -eq 1 ]; then
	echo "USAGE: pullprint FILENAME ..."
	exit 1
fi

GUID=gl23x
P_URL=sc-spooler.campus.gla.ac.uk/PullPrint

which smbclient > /dev/null 2>&1
if [ "$?" -ne "0" ]; then
	echo "ERROR: smbclient not found"
	exit 2
fi

for FN_PRINT in $@
do
	if [ ! -e "$FN_PRINT" ]; then
		echo "ERROR: File ${FN_PRINT} does not exist"
		exit 3
	fi

	smbclient -U campus\\${GUID} //${P_URL} \
					-c "print ${FN_PRINT}"
done

exit 0