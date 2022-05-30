SUMMARY = "IARMMGRS applications"
SECTION = "console/utils"

LICENSE = "Apache-2.0 & ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1d8db96e7ee90f3821eb5e7e913a7b2a"

PV = "${RDK_RELEASE}"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/iarmmgrs;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=iarmmgrs"
SRCREV_iarmmgrs = "${AUTOREV}"
SRCREV_FORMAT = "iarmmgrs"

S = "${WORKDIR}/git"

DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS_append = " safec-common-wrapper"

PARALLEL_MAKE = ""
DEPENDS="qtbase curl yajl dbus iarmbus rdk-logger hdmicecheader devicesettings virtual/devicesettings-hal \
         virtual/iarmmgrs-hal iarmmgrs-hal-headers openssl systemd directfb libsyswrapper rfc libunpriv"
DEPENDS_append_client = " virtual/mfrlib"
RDEPENDS_${PN} += " devicesettings rfc"
RDEPENDS_${PN}_client_morty += " virtual/mfrlib"
RDEPENDS_${PN}_dunfell += "${VIRTUAL-RUNTIME_mfrlib} devicesettings"

ENABLE_PWRMGR2="${@bb.utils.contains('DISTRO_FEATURES', 'pwrmgr2', 'true', 'false', d)}"

inherit pkgconfig breakpad-logmapper syslog-ng-config-gen
SYSLOG-NG_FILTER = "uimgr"
SYSLOG-NG_SERVICE_uimgr += "irmgr.service dsmgr.service pwrmgr.service pwrmgr2.service mfrmgr.service sysmgr.service"
#The log rate and destination are mentioned at iarmbus_git.bb, to avoid duplication of variables set we have commented the below variables.
#SYSLOG-NG_DESTINATION_uimgr = "uimgr_log.txt"
#SYSLOG-NG_LOGRATE_uimgr = "very-high"

CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CXXLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

LDFLAGS += "-lrfcapi -lz"

CFLAGS_append= "${@bb.utils.contains('DISTRO_FEATURES', 'pwrmgr2',' -DUSE_PWR_MGR2', '',d)}"
CXXFLAGS_append= "${@bb.utils.contains('DISTRO_FEATURES', 'pwrmgr2',' -DUSE_PWR_MGR2', '',d)}"
EXTRA_OECONF += " ${@bb.utils.contains('DISTRO_FEATURES', 'pwrmgr2',' --enable-pwrmgr2', '',d)}"

EXTRA_OECONF = " --enable-yocto"
# this component doesn't build with -Wl,-as-needed, remove the flag for now
ASNEEDED = ""

INCLUDE_DIRS = " \
    -I${S}/power/include \
    -I${S}/ir/include \
    -I${S}/mfr/include \
    -I${S}/sysmgr/include \
    -I${S}/dsmgr \
    -I=${includedir}/wdmp-c \
    -I=${includedir}/rdk/iarmbus \
    -I=${includedir}/ccec/drivers/include \
    -I=${includedir}/ccec/drivers/ \
    -I=${includedir} \
    -I=${includedir}/rdk/ds \
    -I=${includedir}/rdk/ds-hal \
    -I=${includedir}/rdk/ds-rpc \
    -I=${includedir}/rdk/iarmmgrs-hal \
    -I=${includedir}/directfb \
    -I=${includedir}/glib-2.0 \
    -I=${libdir}/glib-2.0/include \
    -I${S}/deviceUpdateMgr \
    -I${S}/deviceUpdateMgr/include \
    -I${S}/ipMgr/include \
    -I${S}/vrexmgr/include \
    -I=${includedir}/rdk/servicemanager/helpers \
    -I=${includedir}/rdk/servicemanager \
    "

MFR_LIB ?= '\"libRDKMfrLib.so\"'
MFR_LIB_NAME ?= "-lRDKMfrLib"
# FIXME
# rdk_build.sh has this and we might need to do something with it:
# export _ENABLE_WAKEUP_KEY=-D_ENABLE_WAKEUP_KEY
# export USE_GREEN_PEAK_RF4CE_INTERFACE=-DUSE_GREEN_PEAK_RF4CE_INTERFACE
# export _ENABLE_RESET_LOGIC=-D_ENABLE_RESET_LOGIC
CFLAGS += "-D_ENABLE_RESET_LOGIC -D_ENABLE_WAKEUP_KEY -DENABLE_SD_NOTIFY"

# note: we really on 'make -e' to control LDFLAGS and CFLAGS from here. This is
# far from ideal, but this is to workaround the current component Makefile

# TODO
# FIXME
# Disabling vrexmgr frpm building now since it has dependency with
# green peak header files which is now residing @ rf4ce. Reference:
# JIRA: XRE-6537.
#
LDFLAGS += " -lpthread -lglib-2.0 -ldbus-1 -lIARMBus -lsystemd -lsecure_wrapper -lprivilege"
CFLAGS += "-std=c++11 -fPIC -D_REENTRANT -Wall -I./include ${INCLUDE_DIRS}"

CFLAGS_append_client = " -DMEDIA_CLIENT"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', '-DENABLE_MFR_WIFI', '', d)}"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'build_rne', '-DENABLE_LINUX_REMOTE_KEYS', '', d)}"
CFLAGS_append = " -DUSE_YAJL2"
EXTRA_OEMAKE += "-e MAKEFLAGS="

do_compile() {

    #LDFLAGS="-lsystemd ${LDFLAGS}" CFLAGS="-DENABLE_SD_NOTIFY ${CFLAGS}" oe_runmake -B -C ${S}/sysmgr/
    LDFLAGS="${LDFLAGS}" CFLAGS="-DENABLE_SD_NOTIFY ${CFLAGS}" oe_runmake -B -C ${S}/sysmgr/
    LDFLAGS="-lds -lds-hal -ldshalsrv -ldl ${LDFLAGS}" oe_runmake -B -C ${S}/dsmgr/
    LDFLAGS="-liarmmgrs-ir-hal -ldshalcli -lds ${LDFLAGS}" oe_runmake -B -C ${S}/ir/
    LDFLAGS="-ldshalcli -lds -liarmmgrs-power-hal ${LDFLAGS}" oe_runmake -B -C ${S}/power/

    if [ "${ENABLE_PWRMGR2}" = "true" ] ; then
      LDFLAGS="-ldshalcli -lds -liarmmgrs-power-hal ${LDFLAGS}" oe_runmake -B -C ${S}/pwrmgr2/
    fi

    LDFLAGS="-ldshalcli -lds -liarmmgrs-power-hal ${LDFLAGS}" oe_runmake -B -C ${S}/pwrstate/

    if [ "${@bb.utils.contains('PACKAGECONFIG', 'mfr', 'mfr', '', d)}" != "" ]; then
        export COMCAST_PLATFORM=XI4
        export CFLAGS="${CFLAGS} -DENABLE_SD_NOTIFY -DRDK_MFRLIB_NAME='${MFR_LIB}'"
        export LDFLAGS="${LDFLAGS} ${MFR_LIB_NAME} -lsystemd -ldl"
        oe_runmake -B -C ${S}/mfr
    fi
}

inherit update-rc.d coverity systemd pkgconfig
INITSCRIPT_NAME = "iarmmgrsd"
INITSCRIPT_PARAMS = "defaults 76"

do_install() {

    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}



    for i in sysmgr power ir rdmmgr receiver; do
        install -d ${D}${includedir}/rdk/iarmmgrs/$i
        install -m 0644 ${S}/$i/include/*.h ${D}${includedir}/rdk/iarmmgrs/$i
    done

    install -d ${D}${bindir}
    for i in dsmgr/*Main sysmgr/*Main power/*Main pwrstate/pwrstate_notifier ir/*Main; do
        install -m 0755 ${S}/$i ${D}${bindir}
    done

    if [ "${ENABLE_PWRMGR2}" = "true" ] ; then
        install -m 0755 ${S}/pwrmgr2/*Main2 ${D}${bindir}
    fi

    install -d ${D}${libdir}
    install -m 0755 ${S}/ir/libuinput.so ${D}${libdir}/libuinput.so.0.0.0
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/conf/dsmgr.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/conf/irmgr.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/conf/pwrmgrSwitch.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/conf/pwrmgr.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/conf/pwrmgr.path ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/conf/pwrmgr2.service ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/conf/pwrmgr2.path ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/conf/sysmgr.service ${D}${systemd_unitdir}/system
    install -m 0755 ${S}/conf/start_pwrmgr.sh ${D}${bindir}
    if [ "${ENABLE_PWRMGR2}" = "true" ] ; then
        install -D -m 0644 ${S}/conf/pwrmgr2.conf ${D}${systemd_unitdir}/system/dsmgr.service.d/dsmgr2.conf
    fi
    install -d ${D}${base_libdir}/udev/rules.d/
    install -m 0755 ${S}/ir/59-ir-keyboard.rules ${D}${base_libdir}/udev/rules.d/
    ln -rsf ${D}${libdir}/libuinput.so.0.0.0  ${D}${base_libdir}/libuinput.so

    if [ "${@bb.utils.contains('PACKAGECONFIG', 'mfr', 'mfr', '', d)}" != "" ]; then
        install -d ${D}${includedir}/rdk/iarmmgrs/mfr ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/mfr/include/*.h ${D}${includedir}/rdk/iarmmgrs/mfr
        install -m 0755 ${S}/mfr/*Main ${D}${bindir}
        install -m 0644 ${S}/conf/mfrmgr.service ${D}${systemd_unitdir}/system
    fi
}

do_install_append() {
    install -d ${D}${libdir}
    ln -sf libuinput.so.0.0.0 ${D}${libdir}/libuinput.so
}

#INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

PACKAGECONFIG ??= ""
PACKAGECONFIG[mfr] = "-DUSE_MFR,,,"

SYSTEMD_SERVICE_${PN} += "dsmgr.service"
SYSTEMD_SERVICE_${PN} += "irmgr.service"
SYSTEMD_SERVICE_${PN} += "pwrmgrSwitch.service"
SYSTEMD_SERVICE_${PN} += "pwrmgr.service"
SYSTEMD_SERVICE_${PN} += "pwrmgr.path"
SYSTEMD_SERVICE_${PN} += "pwrmgr2.service"
SYSTEMD_SERVICE_${PN} += "pwrmgr2.path"
SYSTEMD_SERVICE_${PN} += "sysmgr.service"

SYSTEMD_SERVICE_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'mfr', 'mfrmgr.service', '', d)}"
FILES_${PN} += "${systemd_unitdir}/system/*.service"
FILES_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'pwrmgr2','${systemd_unitdir}/system/dsmgr.service.d/dsmgr2.conf', '',d)}"
FILES_${PN} += "${libdir}/*"
FILES_SOLIBSDEV = ""
SOLIBS = ".so"
INSANE_SKIP_${PN} += "dev-so"
# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "dsMgrMain,IARMDaemonMain,pwrMgrMain,irMgrMain,deepSleepMgrMain,mfrMgrMain,sysMgrMain"
BREAKPAD_LOGMAPPER_LOGLIST = "uimgr_log.txt"
