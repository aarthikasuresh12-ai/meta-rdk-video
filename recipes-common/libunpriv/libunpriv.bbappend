DEPENDS += "wdmp-c cimplog rfc"
RDEPENDS_${PN} += " rfc"
LDFLAGS += "-lwdmp-c -lcimplog -lrfcapi"
CPPFLAGS_append = " -I${STAGING_INCDIR}/wdmp-c \
                    -I${STAGING_INCDIR}/cimplog \
                    -D_RDK_VIDEO_PRIV_CAPS_ \
                  "

do_install_append(){
        install -d ${D}${sysconfdir}
        install -d ${D}${sysconfdir}/security/caps/
        install -m 755 ${S}/source/process-capabilities_rdkv.json ${D}${sysconfdir}/security/caps/process-capabilities.json
}

FILES_${PN} += " ${sysconfdir}/security/caps/process-capabilities.json"
