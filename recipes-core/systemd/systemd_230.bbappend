
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
           file://systemd230-journald-syslog-logmissing.patch \
           file://systemd230-remove-srv-dir-check.patch \
           "

#serxione-849, this patch is required to build 64bit kernel(mixed mode)
SRC_URI_append_mixmode = " file://systemd230-build-sys-add-check-for-gperf-lookup-func-signature.patch "
