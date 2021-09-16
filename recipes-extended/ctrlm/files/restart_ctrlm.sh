#!/bin/sh

# stop service if running
systemctl stop ctrlm-main

# kill app if running
CTRLM_PID=$(ps | grep controlMgr | grep -v grep | sed 's/^  *//' | cut -d' ' -f1);

if [ "$CTRLM_PID" != "" ]; then
   kill -INT $CTRLM_PID
fi

/usr/bin/controlMgr &

