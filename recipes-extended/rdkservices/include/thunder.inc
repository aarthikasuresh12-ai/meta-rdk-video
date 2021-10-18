# WPE Framework common
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f1dffbfd5c2eb52e0302eb6296cc3711"
HOMEPAGE = "https://github.com/rdkcentral/Thunder"

SRC_URI = "git://github.com/rdkcentral/Thunder.git;protocol=git;branch=R2;name=thunder"

# Oct 18, 2021
SRCREV_thunder = "ebece0cbcc751fd40b1fd9b358a57a35498fd30d"

PACKAGECONFIG ??= ""
PACKAGECONFIG[debug]            = "-DCMAKE_BUILD_TYPE=Debug,-DCMAKE_BUILD_TYPE=Release,"

PV = "${RDK_RELEASE}+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake pkgconfig

TOOLCHAIN = "gcc"
EXTRA_OECMAKE += "-DCMAKE_SYSROOT=${STAGING_DIR_HOST}"
