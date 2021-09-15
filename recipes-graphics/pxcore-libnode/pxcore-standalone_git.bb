DESCRIPTION = "pxCore"
HOMEPAGE = "http://pxscene.org"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e7948fb185616891f6b4b35c09cd6ba5"

DEPENDS = "openssl freetype westeros util-linux libpng curl rtremote giflib aamp "

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'build_standalone_servicemanager', '', 'servicemanager', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'build_spark_rtremote', '', 'rtremote', d)}"

RDEPENDS_${PN} += "pxcore-libnode"

# return 1 if build_standalone_servicemanager  is set, else 0
def build_standalone_servicemanager(d):
    build_standalone_servicemanager_b = bb.utils.contains('DISTRO_FEATURES', 'build_standalone_servicemanager', '1', '0', d)
    return build_standalone_servicemanager_b

EXTRA_OECMAKE += "-DBUILD_WITH_WAYLAND=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_WESTEROS=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_TEXTURE_USAGE_MONITORING=ON "
EXTRA_OECMAKE += "-DPXCORE_COMPILE_WARNINGS_AS_ERRORS=OFF "
EXTRA_OECMAKE += "-DPXSCENE_COMPILE_WARNINGS_AS_ERRORS=OFF "
EXTRA_OECMAKE += "-DCMAKE_SKIP_RPATH=ON "
EXTRA_OECMAKE += "-DPXCORE_WAYLAND_EGL=ON "
EXTRA_OECMAKE += "-DBUILD_PXSCENE_WAYLAND_EGL=ON "
EXTRA_OECMAKE += "-DPXCORE_MATRIX_HELPERS=OFF "
EXTRA_OECMAKE += "-DBUILD_PXWAYLAND_SHARED_LIB=OFF "
EXTRA_OECMAKE += "-DBUILD_PXWAYLAND_STATIC_LIB=OFF "
EXTRA_OECMAKE += "-DBUILD_PXSCENE_APP_WITH_PXSCENE_LIB=ON "
EXTRA_OECMAKE += "-DPXCORE_ESSOS=ON "
EXTRA_OECMAKE += "-DBUILD_PXSCENE_ESSOS=ON "
EXTRA_OECMAKE += "-DPREFER_SYSTEM_LIBRARIES=ON "
EXTRA_OECMAKE += "-DSERVICEMANAGER_INCLUDE_DIRS=${STAGING_INCDIR}/rdk/servicemanager "
EXTRA_OECMAKE += "-DDISABLE_TURBO_JPEG=ON "
EXTRA_OECMAKE += "-DDISABLE_DEBUG_MODE=ON "
EXTRA_OECMAKE += "-DBUILD_OPTIMUS_STATIC_LIB=ON "
EXTRA_OECMAKE += "-DSUPPORT_DUKTAPE=OFF "
EXTRA_OECMAKE += "-DSPARK_BACKGROUND_TEXTURE_CREATION=ON "
EXTRA_OECMAKE += "-DSPARK_ENABLE_LRU_TEXTURE_EJECTION=ON "
EXTRA_OECMAKE += "-DPXCORE_ESSOS_SETTINGS_SUPPORT=ON "
EXTRA_OECMAKE += "-DSPARK_ENABLE_OPTIMIZED_UPDATE=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_GIF=ON "
EXTRA_OECMAKE += "-DSPARK_ENABLE_VIDEO=ON "
EXTRA_OECMAKE += "-DSPARK_ENABLE_VIDEO_PUNCHTHROUGH=ON "

export PXSCENE_PLATFORM_DEFINES := " -DENABLE_PX_WAYLAND_RPC -DPNG_APNG_SUPPORTED "

def get_pxscene_configuration(d):
    spark_config = bb.utils.contains('DISTRO_FEATURES', 'spark', '-DBUILD_PXSCENE_APP=ON -DBUILD_PXSCENE_STATIC_LIB=OFF -DBUILD_PXSCENE_SHARED_LIB=ON ', '-DBUILD_PXSCENE_APP=OFF -DBUILD_PXSCENE_STATIC_LIB=OFF -DBUILD_PXSCENE_SHARED_LIB=ON -DSUPPORT_NODE=OFF', d)
    return spark_config

EXTRA_OECMAKE += "${@get_pxscene_configuration(d)}"

def is_spark_disabled(d):
    spark_disabled_b = bb.utils.contains('DISTRO_FEATURES', 'spark', '0', '1', d)
    return spark_disabled_b


def get_spark_rev(d):
    spark_rev_s = bb.utils.contains('DISTRO_FEATURES', 'enable_spark_tip', '${AUTOREV}', '51333037650ecee44191492b541106efa573cc35', d)
    return spark_rev_s

def get_spark_git_details(d):
    spark_details_s = bb.utils.contains('DISTRO_FEATURES', 'enable_spark_tip', 'git://github.com/rdkcentral/pxCore;branch=master', 'git://github.com/rdkcentral/pxCore;branch=master', d)
    return spark_details_s

inherit cmake pythonnative

PV = "2.x+git${SRCPV}"

S = "${WORKDIR}/git"

SRC_URI = "${@get_spark_git_details(d)}"
SRCREV = "51333037650ecee44191492b541106efa573cc35"

export PX_BUILD_VERSION := "2.1.0.0"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://archive.patch \
            file://dukluv.git.patch \
            file://0001-nanosvg-patches.patch \
            file://0001-modify-memory-address-space-mapping.patch \
            file://0001-update-memory-address-space-mapping.patch \
            file://fix-optimus-hang.diff \
            file://0001-remove-cors-debug-print.patch \
            file://0001-unregister-wayland-callbacks-on-destroy.patch \
            file://0001-examples-pxScene2d-Include-functional-header.patch \
            file://0001-add-support-s-maxage-parameter.patch \
            file://0001-reconnectReason_fix.patch \
            file://0003-use-timedjoin-in-terminateClient.patch \
            file://0004-set-wayland-clientpid.patch \
            file://DELIA-50375.patch \
            file://0001-updates-for-structure-initialization.patch \
            "

SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'build_spark_rtremote', ' file://rtremote.conf ', '', d)}"
SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'spark', '', ' file://spark_noengine.diff ', d)}"
SRC_URI_append_morty = "${@bb.utils.contains('DISTRO_FEATURES', 'build_spark_rtremote', ' file://unrecognized-command-line-option-fix.patch', '', d)}"

export PXCORE_INCLUDES := "-I${STAGING_INCDIR}/freetype2 -I${STAGING_INCDIR}/pxcore -I${STAGING_INCDIR} -I${STAGING_INCDIR}/usr/include/rdk/servicemanager "
export PXCORE_STAGING := "${STAGING_INCDIR}/freetype2 ${STAGING_INCDIR}/pxcore ${STAGING_INCDIR} ${STAGING_INCDIR}/rdk/servicemanager ${STAGING_INCDIR}/qt5 ${STAGING_INCDIR}/qt5/QtCore ${STAGING_INCDIR}/qt5/QtWidgets "

export PXSCENE_ADDITIONAL_APP_LIBRARIES := "Qt5Core"
export PXSCENE_ADDITIONAL_APP_INCLUDES := " ${STAGING_INCDIR}/qt5 ${STAGING_INCDIR}/qt5/QtCore ${STAGING_INCDIR}/qt5/QtWidgets ${STAGING_INCDIR}/pxcore"
require pxcore-libnode-env.inc

# v8 errors out if you have set CCACHE
CCACHE = ""

def map_nodejs_arch(a, d):
    import re

    if   re.match('p(pc|owerpc)(|64)', a): return 'ppc'
    elif re.match('i.86$', a): return 'ia32'
    elif re.match('x86_64$', a): return 'x64'
    elif re.match('(arm|aarch)64$', a): return 'arm64'
    return a

ARCHFLAGS_append_arm = "${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', '--with-arm-float-abi=hard', '--with-arm-float-abi=softfp', d)}"
ARCHFLAGS_append_mipsel = " --with-mips-arch-variant=r1"
ARCHFLAGS ?= ""
SELECTED_OPTIMIZATION_remove = "-O1"
SELECTED_OPTIMIZATION_remove = "-O2"
SELECTED_OPTIMIZATION_append = " -Os"
SELECTED_OPTIMIZATION_append = " -Wno-deprecated-declarations -Wno-maybe-uninitialized -Wno-address"

TARGET_CFLAGS += " -fno-delete-null-pointer-checks -fpermissive -Os -DPNG_APNG_SUPPORTED "
TARGET_CXXFLAGS += " -fno-delete-null-pointer-checks -fpermissive -Os -DPNG_APNG_SUPPORTED "
TARGET_CXXFLAGS += " -Wl,--warn-unresolved-symbols "

def get_mode(d):
    mode = d.getVar("PXCORE_DEVELOPER_MODE", True) or '0'
    #return mode
    return 1 #todo update to support static

# return 1 if pxscene is excluded from build, else 0
def is_pxscene_excluded(d):
    pxscene_excluded_b = bb.utils.contains('DISTRO_FEATURES', 'exclude_pxscene', '1', '0', d)
    return pxscene_excluded_b

# return 1 if unit test is enabled, else 0
def build_px_tests(d):
    build_px_tests_b = bb.utils.contains('DISTRO_FEATURES', 'enable_unit_test', '1', '0', d)
    return build_px_tests_b


# return 1 if pxbenchmark is enabled, else 0
def build_px_benchmark(d):
    build_px_benchmark_b = '0'
    enabled_by_pxbenchmark_distro_b = bb.utils.contains('DISTRO_FEATURES', 'enable_pxbenchmark', '1', '0', d)
    enabled_by_firebolt_test_client_distro_b = bb.utils.contains('DISTRO_FEATURES', 'firebolt_test_client', '1', '0', d)
    if enabled_by_pxbenchmark_distro_b == '1' or enabled_by_firebolt_test_client_distro_b == '1':
        build_px_benchmark_b = '1'
    return build_px_benchmark_b

# return 1 if duktape is disabled, else 0
def is_duktape_disabled(d):
    duktape_disabled = bb.utils.contains('DISTRO_FEATURES', 'disable_pxscene_duktape', '1', '0', d)
    return duktape_disabled

def get_px_duktape_configuration(d):
    if is_duktape_disabled(d) == '1':
        return "-DSUPPORT_DUKTAPE=OFF -DBUILD_DUKTAPE=OFF"
    return "-DSUPPORT_DUKTAPE=OFF -DBUILD_DUKTAPE=OFF"
    
def get_px_tests_configuration(d):
    if build_px_tests(d) == '1' and get_mode(d) == '0':
        return "-DBUILD_PX_TESTS=ON "
    return "-DBUILD_PX_TESTS=OFF "

def get_pxbenchmark_configuration(d):
    if build_px_benchmark(d) == '1':
        return "-DBUILD_PXBENCHMARK=ON "
    return "-DBUILD_PXBENCHMARK=OFF "

# return 1 if rne is enabled, else 0
def is_rne_enabled(d):
    rne_enabled = bb.utils.contains('DISTRO_FEATURES', 'build_rne', '1', '0', d)
    return rne_enabled

def get_px_rne_configuration(d):
        return "-DBUILD_WITH_SERVICE_MANAGER=ON -DBUILD_WITH_SERVICE_MANAGER_LINKED=OFF "

# return 1 if spark_rtremote is enabled, else 0
def build_spark_rtremote(d):
    build_spark_rtremote_b = bb.utils.contains('DISTRO_FEATURES', 'build_spark_rtremote', '1', '0', d)
    return build_spark_rtremote_b

EXTRA_OECMAKE += "${@get_px_tests_configuration(d)}"
EXTRA_OECMAKE += "${@get_px_duktape_configuration(d)}"
EXTRA_OECMAKE += "${@get_px_rne_configuration(d)}"
EXTRA_OECMAKE += "${@get_pxbenchmark_configuration(d)}"
#PARALLEL_MAKE = ""

# V8's JIT infrastructure requires binaries such as mksnapshot and
# mkpeephole to be run in the host during the build. However, these
# binaries must have the same bit-width as the target (e.g. a x86_64
# host targeting ARMv6 needs to produce a 32-bit binary). Instead of
# depending on a third Yocto toolchain, we just build those binaries
# for the target and run them on the host with QEMU.
python do_create_v8_qemu_wrapper () {
    """Creates a small wrapper that invokes QEMU to run some target V8 binaries
    on the host."""

    qemu_binary=qemu_target_binary(d)
    if qemu_binary == "qemu-allarch":
        qemu_binary = "qemuwrapper"

    qemu_options = d.getVar("QEMU_OPTIONS", True)

    rootfs_path=d.getVar('STAGING_DIR_HOST', True)
    libdir_qemu=d.expand('${STAGING_DIR_HOST}${libdir}')
    base_libdir_qemu=d.expand('${STAGING_DIR_HOST}${base_libdir}')

    qemu_cmd= qemu_binary + " " + qemu_options + " -L " + rootfs_path \
            + " -E LD_LIBRARY_PATH=" + libdir_qemu + ":"+ base_libdir_qemu + " "

    wrapper_path = d.expand('${S}/examples/pxScene2d/external/v8-qemu-wrapper.sh')
    with open(wrapper_path, 'w') as wrapper_file:
        wrapper_file.write("""#!/bin/sh

# This file has been generated automatically.
# It invokes QEMU to run binaries built for the target in the host during the
# build process.

%s "$@"
""" % qemu_cmd)
    os.chmod(wrapper_path, 0o755)
}

inherit qemu
do_create_v8_qemu_wrapper[dirs] = "${B}"
addtask create_v8_qemu_wrapper after do_patch before do_configure

SRC_URI += " file://node-v10.15.3_mods.patch file://openssl_1.0.2_compatibility.patch file://node-v10.15.3_qemu_wrapper.patch "
DEPENDS += " qemu-native "
CFLAGS += " -latomic "
CXXFLAGS += " -latomic "
TARGET_LDFLAGS += " -Wl,--no-as-needed -latomic -Wl,--as-needed "
TARGET_CFLAGS += " -latomic "
TARGET_CXXFLAGS += " -latomic "



# Node is way too cool to use proper autotools, so we install two wrappers to forcefully inject proper arch cflags to workaround gypi
do_configure_append () {
    if [ "${@is_spark_disabled(d)}" -eq '0' ]
    then
      export LD="${CXX}"
      GYP_DEFINES="${GYP_DEFINES}" export GYP_DEFINES
      # $TARGET_ARCH settings don't match --dest-cpu settings
      cd ${S}/examples/pxScene2d/external/
      rm -rf libnode
      rm -rf node
      if [ ! -L ${S}/examples/pxScene2d/external/libnode-v10.15.3 ]; then
          if [ -d ${S}/../../../node_pxsa ]; then
              rm -rf ${S}/../../../node_pxsa
          fi
          cp -R ${S}/examples/pxScene2d/external/libnode-v10.15.3 ${S}/../../../node_pxsa
          rm -rf ${S}/examples/pxScene2d/external/libnode-v10.15.3
          ln -sf ${S}/../../../node_pxsa libnode-v10.15.3
      fi
      cp -R ${S}/examples/pxScene2d/external/v8-qemu-wrapper.sh ${S}/../../../.
      ln -sf ${S}/../../../node_pxsa libnode
      ln -sf ${S}/../../../node_pxsa node
      cd ${S}/../../../node_pxsa

      ./configure --prefix=${prefix} --without-snapshot --shared-openssl --without-intl --without-inspector --shared \
                 --dest-cpu="${@map_nodejs_arch(d.getVar('TARGET_ARCH', True), d)}" \
                 --dest-os=linux \
                 ${ARCHFLAGS}
    fi
}

do_compile_prepend () {
    if [ "${@is_spark_disabled(d)}" -eq '0' ]
    then
      export LD="${CXX}"
      cd ${S}/../../../node_pxsa

      MAKEFLAGS="$MAKEFLAGS ${PARALLEL_MAKE}" make -C ${S}/examples/pxScene2d/external/libnode/
      if [ "${@get_mode(d)}" -eq '1' ]
      then
          ln -sf ${S}/../../../node_pxsa/out/Release/obj.target/libnode.so.64 ${S}/../../../node_pxsa/libnode.so
          ln -sf libnode.so.64 ${S}/../../../node_pxsa/out/Release/obj.target/libnode.so
      fi
    fi

    if [ "${@build_spark_rtremote(d)}" -eq '1' ]
    then
    	make -C ${S}/remote/ -f Makefile librtRemote.so
    	make -C ${S}/remote/ -f Makefile librtRemote_s.a
    fi

    if [ "${@is_spark_disabled(d)}" -eq '0' ]
    then
      if [ "${@build_px_benchmark(d)}" -eq '1' ]
      then
        mkdir -p ${S}/examples/pxBenchmark/external/Celero/temp
        cd ${S}/examples/pxBenchmark/external/Celero/temp
        cmake ..
        cmake --build . --config Release
        cd ${S}
      fi
    fi
}

do_install () {
   install -d ${D}/usr/lib
   cp -dr ${S}/examples/pxScene2d/src/libSpark* ${D}/usr/lib
   ln -sf libSpark.so ${S}/examples/pxScene2d/src/libpxscene.so
   cp -dr ${S}/examples/pxScene2d/src/libpxscene* ${D}/usr/lib
   if [ "${@is_spark_disabled(d)}" -eq '0' ]
   then
     #add below line later if we move for static builds
     #install -d ${D}/usr/lib/dukluv
     install -d ${D}${includedir}
     install -d ${D}${bindir}/tests

     #find ${S}/build -name libpxCore* | xargs -I {} cp -dr {} ${D}/usr/lib


     #Library Available only when optimusprime feature is enabled
     if [ -f  ${S}/examples/pxScene2d/src/liboptimus* ]; then
     cp -dr ${S}/examples/pxScene2d/src/liboptimus* ${D}/usr/lib
     fi

     #libnode pxcore files
     install -d ${D}/home/root
     cp -dr ${S}/examples/pxScene2d/src/Spark ${D}/home/root
     ln -sf Spark ${S}/examples/pxScene2d/src/pxscene
     cp -dr ${S}/examples/pxScene2d/src/pxscene ${D}/home/root
     #cp -r ${S}/examples/pxScene2d/src/node_modules ${D}/home/root
     #cp -r ${S}/examples/pxScene2d/src/rcvrcore ${D}/home/root
     #cp -r ${S}/examples/pxScene2d/src/browser ${D}/home/root
     #cp ${S}/examples/pxScene2d/src/*.ttf ${D}/home/root
     #cp ${S}/examples/pxScene2d/src/*.json ${D}/home/root
     #cp ${S}/examples/pxScene2d/src/*.js ${D}/home/root
     #cp ${S}/examples/pxScene2d/src/waylandregistry.conf ${D}/home/root

     if [ -f  ${S}/examples/pxBenchmark/src/pxbenchmark ]; then

	  if [ "${@bb.utils.contains('DISTRO_FEATURES', 'enable_pxbenchmark', 'true', 'false', d)}" = "true" ]
	  then
	   	  cp -dr ${S}/examples/pxBenchmark/src/pxbenchmark ${D}/home/root
	  	  rm -f ${D}/home/root/Spark
	  fi
	  if [ "${@bb.utils.contains('DISTRO_FEATURES', 'firebolt_test_client', 'true', 'false', d)}" = "true" ]
	  then
		#install a copy of the binary to staging directory so that firebolt-test-client recipe can include this binary for testing
		install -d ${STAGING_DIR_TARGET}/${bindir}/pxbenchmarkbin_for_fireboltTest/
		cp -dr ${S}/examples/pxBenchmark/src/pxbenchmark ${STAGING_DIR_TARGET}/${bindir}/pxbenchmarkbin_for_fireboltTest/
	  fi
       fi
   fi

   #mkdir -p ${D}/etc
   #install -m 0644 "${WORKDIR}/rtremote.conf" "${D}/etc/"
}

pxcore_extra_clean() {
    rm -rf ${WORKDIR}/../../node_pxsa
}
do_clean[prefuncs] += "pxcore_extra_clean"

FILES_${PN} += "/home/root"
FILES_${PN} += "${libdir}/*.so"
FILES_${PN} += "${bindir}/tests/*"
FILES_${PN}-dbg += "${bindir}/tests/.debug"
FILES_${PN}-dbg += "/home/root/.debug"
FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} += "dev-so staticdev"
INSANE_SKIP_${PN}_append_morty = " ldflags"
DEBIAN_NOAUTONAME_${PN} = "1"

BBCLASSEXTEND = "native"


