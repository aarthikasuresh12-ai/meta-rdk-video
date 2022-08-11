# WPE Framework common
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f1dffbfd5c2eb52e0302eb6296cc3711"
HOMEPAGE = "https://github.com/rdkcentral/Thunder"

SRC_URI = "git://github.com/rdkcentral/Thunder.git;protocol=git;branch=R2;name=thunder"

# Jul 11, 2022
SRCREV_thunder = "02f6ccbca6832043cbc94cc28c1bda7d4954a4e6"

PACKAGECONFIG ??= ""
PACKAGECONFIG[debug]            = "-DCMAKE_BUILD_TYPE=Debug,-DCMAKE_BUILD_TYPE=Release,"

PV = "${RDK_RELEASE}"
S = "${WORKDIR}/git"

inherit cmake pkgconfig

TOOLCHAIN = "gcc"
EXTRA_OECMAKE += "-DCMAKE_SYSROOT=${STAGING_DIR_HOST}"
