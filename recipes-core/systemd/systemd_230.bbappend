
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
           file://systemd230-journald-syslog-logmissing.patch \
           file://systemd230-remove-srv-dir-check.patch \
           "