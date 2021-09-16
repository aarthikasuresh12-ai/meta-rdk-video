SUMMARY = "Sys mon tool key simulator recipe"

DESCRIPTION = "Sys mon tool key simulator recipe"

SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV = "${AUTOREV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/sys_mon_tools/iarm_query_powerstate;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
S = "${WORKDIR}/git"

DEPENDS = "iarmbus iarmmgrs dbus glib-2.0"

inherit autotools pkgconfig coverity

do_install() {
        install -d ${D}${bindir}
        install -m 0755 ${B}/QueryPowerState ${D}${bindir}/
        # Create a symbolic link in / as the current automation team testing scripts
        # expect binaries to be present in root
        ln -sf ${bindir}/QueryPowerState ${D}/QueryPowerState
}

FILES_${PN} = "${bindir}/QueryPowerState \
               /QueryPowerState"
