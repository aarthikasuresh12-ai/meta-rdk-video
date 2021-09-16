PACKAGECONFIG_append = " tr181set"
PACKAGECONFIG[tr181set] = "--enable-tr181set=yes"
DEPENDS+="iarmbus tr69hostif-headers wdmp-c curl "

do_install_append() {
      install -m 0755 ${S}/getRFC.sh ${D}${base_libdir}/rdk
      install -m 0755 ${S}/isFeatureEnabled.sh ${D}${base_libdir}/rdk
      ln -sf ${bindir}/tr181 ${D}${bindir}/tr181Set
}

FILES_${PN} += "${bindir}/tr181"
FILES_${PN} += "${bindir}/tr181Set"
FILES_${PN} += "${libdir}/*"
