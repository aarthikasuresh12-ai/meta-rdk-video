SUMMARY = "This receipe compiles the audioserver component"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc"

S = "${WORKDIR}/git"

DEPENDS = ""

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/audioserver/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=audioserverheaders"
PV = "${RDK_RELEASE}"
SRCREV_audioserverheaders = "${AUTOREV}"
SRCREV_FORAMAT = "audioserverheaders"

do_compile[noexec] = "1"

do_install() {
        install -d ${D}${includedir}
        install -m 0644 ${S}/audioserver/include/audioserver.h ${D}${includedir}
        install -m 0644 ${S}/audioserver/include/audioserver-soc.h ${D}${includedir}
        install -m 0644 ${S}/gstplugin/gstaudsrvsink.h ${D}${includedir}
        install -m 0644 ${S}/gstplugin/gstaudsrvsink-socapi.h ${D}${includedir}
}

ALLOW_EMPTY_${PN} = "1"

