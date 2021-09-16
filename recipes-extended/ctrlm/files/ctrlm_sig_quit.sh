#!/bin/sh

# kill app if running
CTRLM_PID=$(ps | grep controlMgr | grep -v grep | sed 's/^  *//' | cut -d' ' -f1);

if [ "$CTRLM_PID" != "" ]; then
   kill -QUIT $CTRLM_PID
fi
