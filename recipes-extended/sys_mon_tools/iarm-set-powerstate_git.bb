SUMMARY = "Sys mon tool - IARM SET POWERSTATE recipe"

DESCRIPTION = "Sys mon tool - IARM SET POWERSTATE recipe"

SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV = "${AUTOREV}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/sys_mon_tools/iarm_set_powerstate;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
S = "${WORKDIR}/git"

DEPENDS = "iarmbus iarmmgrs dbus glib-2.0"

inherit autotools pkgconfig coverity 

do_install() {
        install -d ${D}${bindir}
        install -m 0755 ${B}/SetPowerState ${D}${bindir}/
        # Create a symbolic link in / as the current automation team testing scripts
        # expect binaries to be present in root
        ln -sf ${bindir}/SetPowerState ${D}/SetPowerState
}

FILES_${PN} = "${bindir}/SetPowerState \
               /SetPowerState"
