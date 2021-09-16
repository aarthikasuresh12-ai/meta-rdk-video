
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
           file://systemd216-journald-syslog-logmissing.patch \
           "