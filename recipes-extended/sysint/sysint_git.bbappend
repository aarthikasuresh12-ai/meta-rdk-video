SYSTEMD_VERSION = "${PREFERRED_VERSION_systemd}"
DUNFELL_BUILD = "${@bb.utils.contains('DISTRO_FEATURES', 'dunfell', 'true', 'false', d)}"

do_install_append_client() {

   ## The systemd-timesyncd version 244(dunfell) and above creates "/run/systemd/timesync/synchronized" and the versions below 244
   ## creates "/tmp/clock-event" flag when system time is synced. Update ntp-event path unit accordingly
   if [ "${DUNFELL_BUILD}" = "true" ] && [ "${SYSTEMD_VERSION}" = "1:230%" ]; then
      sed -i -e 's|.*PathExists=.*|PathExists=/tmp/clock-event|g' ${D}${systemd_unitdir}/system/ntp-event.path
   fi

}
