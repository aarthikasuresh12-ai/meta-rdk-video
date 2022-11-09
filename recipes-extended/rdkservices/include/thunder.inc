# WPE Framework common
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f1dffbfd5c2eb52e0302eb6296cc3711"
HOMEPAGE = "https://github.com/rdkcentral/Thunder"

THUNDER_RELEASE_TAG_NAME = "R2-v1.12"

SRC_URI = "git://github.com/rdkcentral/Thunder.git;protocol=git;branch=R2;name=thunder"

# Sep 29, 2022, 82d4791478934292f7260fef233b9743fa34d88e
SRCREV_thunder = "${THUNDER_RELEASE_TAG_NAME}"

PACKAGECONFIG ??= ""
PACKAGECONFIG[debug]            = "-DCMAKE_BUILD_TYPE=Debug,-DCMAKE_BUILD_TYPE=Release,"

PV = "${RDK_RELEASE}+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake pkgconfig

TOOLCHAIN = "gcc"
EXTRA_OECMAKE += "-DCMAKE_SYSROOT=${STAGING_DIR_HOST}"
