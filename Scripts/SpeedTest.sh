(LANG=C dd if=/dev/zero of=benchtest_$$ bs=1G count=20 conv=fdatasync iflag=dsync) 2>&1 | awk -F, '{io=$NF} END { print io}' | sed 's/^[ \t]*//;s/[ \t]*$//' &&
(LANG=C dd if=benchtest_$$ of=/dev/null bs=1G count=20 conv=fdatasync iflag=dsync) 2>&1 | awk -F, '{io=$NF} END { print io}' | sed 's/^[ \t]*//;s/[ \t]*$//' &&
rm -f benchtest_$$