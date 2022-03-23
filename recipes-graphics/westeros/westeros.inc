LICENSE = "Apache-2.0"

PV = "1.0+gitr${SRCPV}"

SRC_URI = "${WESTEROS_URI}"
SRCREV = "${WESTEROS_SRCREV}"

WESTEROS_URI ?= "${CMF_GIT_ROOT}/components/opensource/westeros;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_MASTER_BRANCH};name=westeros"
# Tip of westeros master as of Jan 27, 2022
WESTEROS_SRCREV = "a19dc5bd5ec39c357a9fc7b0e4bb7c709b6b6e59"

LICENSE_LOCATION ?= "${S}/LICENSE"
LIC_FILES_CHKSUM = "file://${LICENSE_LOCATION};md5=8fb65319802b0c15fc9e0835350ffa02"

SRCREV_FORMAT = "westeros"
