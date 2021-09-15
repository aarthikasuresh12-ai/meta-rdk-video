DESCRIPTION = "pxCore"
HOMEPAGE = "http://pxscene.org"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e7948fb185616891f6b4b35c09cd6ba5"

DEPENDS = "openssl freetype westeros util-linux libpng curl rtremote giflib libjpeg-turbo"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'build_spark_rtremote', '', 'rtremote', d)}"

#EXTRA_OECMAKE = "-DWINDOWLESS_EGL=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_WAYLAND=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_WESTEROS=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_TEXTURE_USAGE_MONITORING=ON "
#EXTRA_OECMAKE += "-DBUILD_WITH_WINDOWLESS_EGL=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_SERVICE_MANAGER=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_SERVICE_MANAGER_LINKED=ON "
EXTRA_OECMAKE += "-DPXCORE_COMPILE_WARNINGS_AS_ERRORS=OFF "
EXTRA_OECMAKE += "-DPXSCENE_COMPILE_WARNINGS_AS_ERRORS=OFF "
EXTRA_OECMAKE += "-DCMAKE_SKIP_RPATH=ON "
EXTRA_OECMAKE += "-DPXCORE_WAYLAND_EGL=ON "
EXTRA_OECMAKE += "-DBUILD_PXSCENE_WAYLAND_EGL=ON "
EXTRA_OECMAKE += "-DPXCORE_ESSOS=ON "
EXTRA_OECMAKE += "-DBUILD_PXSCENE_ESSOS=ON "
EXTRA_OECMAKE += "-DPREFER_SYSTEM_LIBRARIES=ON "
EXTRA_OECMAKE += "-DDISABLE_TURBO_JPEG=ON "
EXTRA_OECMAKE += "-DDISABLE_DEBUG_MODE=ON "
EXTRA_OECMAKE += "-DPXCORE_WAYLAND_EGL=ON "
EXTRA_OECMAKE += "-DBUILD_PXSCENE_WAYLAND_EGL=ON "
EXTRA_OECMAKE += "-DPXCORE_ESSOS=ON "
EXTRA_OECMAKE += "-DBUILD_PXSCENE_ESSOS=ON "
EXTRA_OECMAKE += "-DSPARK_BACKGROUND_TEXTURE_CREATION=ON "
EXTRA_OECMAKE += "-DSPARK_ENABLE_LRU_TEXTURE_EJECTION=ON "
EXTRA_OECMAKE += "-DPXCORE_ESSOS_SETTINGS_SUPPORT=OFF "
EXTRA_OECMAKE += "-DSPARK_ENABLE_OPTIMIZED_UPDATE=ON "
EXTRA_OECMAKE += "-DBUILD_WITH_GIF=ON "

export PXSCENE_PLATFORM_DEFINES := " -DENABLE_PX_WAYLAND_RPC -DPNG_APNG_SUPPORTED "

def get_pxscene_configuration(d):
    spark_config = bb.utils.contains('DISTRO_FEATURES', 'spark', '-DBUILD_PXSCENE_APP=OFF -DBUILD_PXSCENE_STATIC_LIB=OFF -DBUILD_PXSCENE_SHARED_LIB=ON ', '-DBUILD_PXSCENE_APP=OFF -DBUILD_PXSCENE_STATIC_LIB=OFF -DBUILD_PXSCENE_SHARED_LIB=ON -DSUPPORT_NODE=OFF ', d)
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

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://archive.patch \
            file://dukluv.git.patch \
            file://0001-nanosvg-patches.patch \
            file://0001-modify-memory-address-space-mapping.patch \
            file://0001-update-memory-address-space-mapping.patch \
            file://0001-remove-cors-debug-print.patch \
            file://0001-unregister-wayland-callbacks-on-destroy.patch \
            file://0001-unregister-compositor-on-destroy.patch \
            file://0001-add-support-s-maxage-parameter.patch \
            file://0001-reconnectReason_fix.patch \
            file://DELIA-50375.patch \
            file://0001-updates-for-structure-initialization.patch \
            "

SRC_URI += "file://0001-disable-extra-main.patch "


SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'build_spark_rtremote', ' file://rtremote.conf ', '', d)}"
SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'spark', '', ' file://spark_noengine.diff ', d)}"
SRC_URI_append = "${@bb.utils.contains('DISTRO_FEATURES', 'build_spark_rtremote', ' file://unrecognized-command-line-option-fix.patch', '', d)}"

#FIXME localstorage temp enabled for all firebolt based builds
SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'build_rne',' file://temp_enable_localstorage_wpe.patch ', '', d)}"

export PXCORE_INCLUDES := "-I${STAGING_INCDIR}/freetype2 -I${STAGING_INCDIR}/pxcore -I${STAGING_INCDIR} "
export PXCORE_STAGING := "${STAGING_INCDIR}/pxcore ${STAGING_INCDIR}/freetype2 ${STAGING_INCDIR}"
export PXSCENE_ADDITIONAL_APP_INCLUDES := " ${STAGING_INCDIR}/pxcore "

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

def get_mode(d):
    mode = d.getVar("PXCORE_DEVELOPER_MODE", True) or '0'
    return 1

# return 1 if pxscene is excluded from build, else 0
def is_pxscene_excluded(d):
    pxscene_excluded_b = bb.utils.contains('DISTRO_FEATURES', 'exclude_pxscene', '1', '0', d)
    return pxscene_excluded_b

# return 1 if unit test is enabled, else 0
def build_px_tests(d):
    build_px_tests_b = bb.utils.contains('DISTRO_FEATURES', 'enable_unit_test', '1', '0', d)
    return build_px_tests_b

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

# return 1 if spark_rtremote is enabled, else 0
def build_spark_rtremote(d):
    build_spark_rtremote_b = bb.utils.contains('DISTRO_FEATURES', 'build_spark_rtremote', '1', '0', d)
    return build_spark_rtremote_b

EXTRA_OECMAKE += "${@get_px_tests_configuration(d)}"
EXTRA_OECMAKE += "${@get_px_duktape_configuration(d)}"

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
          if [ -d ${S}/../../../node_libnode ]; then
              rm -rf ${S}/../../../node_libnode
          fi
          cp -R ${S}/examples/pxScene2d/external/libnode-v10.15.3 ${S}/../../../node_libnode
          rm -rf ${S}/examples/pxScene2d/external/libnode-v10.15.3
          ln -sf ${S}/../../../node_libnode libnode-v10.15.3
      fi
      cp -R ${S}/examples/pxScene2d/external/v8-qemu-wrapper.sh ${S}/../../../.
      ln -sf ${S}/../../../node_libnode libnode
      ln -sf ${S}/../../../node_libnode node
      cd ${S}/../../../node_libnode
      pwd
      ls -lrt

      if [ "${@get_mode(d)}" -eq '1' ]
      then
	  ./configure --prefix=${prefix} --without-snapshot --shared-openssl --without-intl --without-inspector --shared \
                 --dest-cpu="${@map_nodejs_arch(d.getVar('TARGET_ARCH', True), d)}" \
                 --dest-os=linux \
                 ${ARCHFLAGS}
      else
	  ./configure --prefix=${prefix} --without-snapshot --shared-openssl --without-intl --without-inspector --enable-static \
                 --dest-cpu="${@map_nodejs_arch(d.getVar('TARGET_ARCH', True), d)}" \
                 --dest-os=linux \
                 ${ARCHFLAGS}
      fi
    fi
}

do_compile_prepend () {
    if [ "${@is_spark_disabled(d)}" -eq '0' ]
    then
      cd ${S}/../../../node_libnode
      export LD="${CXX}"

      MAKEFLAGS="$MAKEFLAGS ${PARALLEL_MAKE}" make -C ${S}/examples/pxScene2d/external/libnode/

      if [ "${@get_mode(d)}" -eq '1' ]
      then
          ln -sf ${S}/../../../node_libnode/out/Release/obj.target/libnode.so.64 ${S}/../../../node_libnode/libnode.so
          ln -sf libnode.so.64 ${S}/../../../node_libnode/out/Release/obj.target/libnode.so
      fi
    fi
    if [ "${@build_spark_rtremote(d)}" -eq '1' ]
    then
    	make -C ${S}/remote/ -f Makefile librtRemote.so
    	make -C ${S}/remote/ -f Makefile librtRemote_s.a
    fi
}

do_install () {
   install -d ${D}/usr/lib
   #uncomment if static libs are supported
   #install -d ${D}/usr/lib/dukluv
   install -d ${D}${includedir}
   find ${S}/build -name libpxCore* | xargs -I {} cp -dr {} ${D}/usr/lib
   if [ "${@is_spark_disabled(d)}" -eq '0' ]
   then
     install -d ${D}${bindir}/tests

     if [ "${@get_mode(d)}" -eq '1' ]
     then
          if [ "${@is_pxscene_excluded(d)}" -eq '0' ]
          then
	    cp -dr ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/libnode.so* ${D}/usr/lib
          fi
     else
          if [ "${@is_pxscene_excluded(d)}" -eq '0' ]
          then
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/libnode.a ${D}/usr/lib
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/deps/http_parser/libhttp_parser.a ${D}/usr/lib
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/deps/uv/libuv.a ${D}/usr/lib
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/deps/cares/libcares.a ${D}/usr/lib/libcares-libnode.a
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/deps/v8/tools/gyp/libv8_base.a ${D}/usr/lib
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/deps/v8/tools/gyp/libv8_libbase.a ${D}/usr/lib
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/deps/v8/tools/gyp/libv8_libplatform.a ${D}/usr/lib
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/deps/v8/tools/gyp/libv8_nosnapshot.a ${D}/usr/lib
	      cp ${S}/examples/pxScene2d/external/libnode/out/Release/obj.target/deps/v8_inspector/third_party/v8_inspector/platform/v8_inspector/libv8_inspector_stl.a ${D}/usr/lib
              #uncomment below lines if static libs are supported
	      #cp ${S}/examples/pxScene2d/external/dukluv/build/libduv.a ${D}/usr/lib/dukluv/
	      #cp ${S}/examples/pxScene2d/external/dukluv/build/libdschema.a ${D}/usr/lib/dukluv/
	      #cp ${S}/examples/pxScene2d/external/dukluv/build/libduktape.a ${D}/usr/lib/dukluv/
	      #cp ${S}/examples/pxScene2d/external/dukluv/build/libuv.a ${D}/usr/lib/dukluv/

              if [ "${@build_px_tests(d)}" -eq '1' ]

              then
                 install -m 0755 ${S}/tests/pxScene2d/pxscene2dtests ${D}${bindir}/tests
              fi 
          fi
     fi
   fi
   cp -dr ${S}/examples/pxScene2d/src/libpxwayland* ${D}/usr/lib
   cp -dr ${S}/build/egl/librtCore* ${D}/usr/lib
    if [ "${@build_spark_rtremote(d)}" -eq '1' ]
    then
    	cp -dr ${S}/remote/librtRemote* ${D}/usr/lib
    fi

   mkdir -p ${D}${includedir}/pxcore
    if [ "${@build_spark_rtremote(d)}" -eq '1' ]
    then
    	install -m 0644 ${S}/src/*.h ${D}${includedir}/pxcore
    fi
   install -m 0644 ${S}/examples/pxScene2d/src/*.h ${D}${includedir}/pxcore

   if [ "${@is_spark_disabled(d)}" -eq '0' ]
   then
     mkdir -p ${D}${includedir}/pxcore/rtScriptNode
     mkdir -p ${D}${includedir}/pxcore/rtScriptV8
     install -m 0644 ${S}/src/rtScriptV8/*.h ${D}${includedir}/pxcore/rtScriptNode
     install -m 0644 ${S}/src/rtScriptV8/*.h ${D}${includedir}/pxcore/rtScriptV8
     if [ "${@is_duktape_disabled(d)}" -eq '0' ]
     then
        mkdir -p ${D}${includedir}/pxcore/rtScriptDuk
        install -m 0644 ${S}/src/rtScriptDuk/*.h ${D}${includedir}/pxcore/rtScriptDuk
     fi
   fi
   if [ "${@build_spark_rtremote(d)}" -eq '1' ]
   then
     mkdir -p ${D}${includedir}/pxcore/unix
     install -m 0644 ${S}/src/unix/*.h ${D}${includedir}/pxcore/unix

     mkdir -p ${D}${includedir}/pxcore/gles
     install -m 0644 ${S}/src/gles/*.h ${D}${includedir}/pxcore/gles
   fi

   if [ "${@is_spark_disabled(d)}" -eq '0' ]
   then
     mkdir -p ${D}${includedir}/libnode
     install -m 0644 ${S}/examples/pxScene2d/external/libnode/src/*.h ${D}${includedir}/libnode

     mkdir -p ${D}${includedir}/libnode/deps/uv
     install -m 0644 ${S}/examples/pxScene2d/external/libnode/deps/uv/include/*.h ${D}${includedir}/libnode/deps/uv

     mkdir -p ${D}${includedir}/libnode/deps/uv/uv
     cp -r ${S}/examples/pxScene2d/external/libnode/deps/uv/include/uv ${D}${includedir}/libnode/deps/uv/
     mkdir -p ${D}${includedir}/libnode/tracing
     install -m 0644 ${S}/examples/pxScene2d/external/libnode/src/tracing/*.h ${D}${includedir}/libnode/tracing

     mkdir -p ${D}${includedir}/libnode/deps/v8

     mkdir -p ${D}${includedir}/libnode/deps/cares
     install -m 0644 ${S}/examples/pxScene2d/external/libnode/deps/cares/include/*.h ${D}${includedir}/libnode/deps/cares

     mkdir -p ${D}${includedir}/libnode/deps/v8/include
     cp -r ${S}/examples/pxScene2d/external/libnode/deps/v8/include/ ${D}${includedir}/libnode/deps/v8/
   fi
   if [ "${@build_spark_rtremote(d)}" -eq '1' ]
   then
     cp -dr ${S}/src/rt*.h ${D}${includedir}/pxcore
     cp -dr ${S}/remote/rtRemote.h ${D}${includedir}/pxcore
     cp -R ${S}/remote/rapidjson/ ${D}${includedir}/pxcore
   fi
   install -d ${D}/home/root
   cp ${S}/examples/pxScene2d/src/*.json ${D}/home/root
   if [ "${@is_spark_disabled(d)}" -eq '0' ]
   then
     #libnode pxcore files
     cp -r ${S}/examples/pxScene2d/src/node_modules ${D}/home/root
     cp -r ${S}/examples/pxScene2d/src/browser ${D}/home/root
     cp -r ${S}/examples/pxScene2d/src/rcvrcore ${D}/home/root
     cp ${S}/examples/pxScene2d/src/*.js ${D}/home/root

     #if [ "${@is_duktape_disabled(d)}" -eq '0' ]
     #then
        #cp -r ${S}/examples/pxScene2d/src/duk_modules ${D}/home/root
     #fi
     cp ${S}/examples/pxScene2d/src/*.ttf ${D}/home/root
     cp ${S}/tests/pxScene2d/testRunner/tests.json ${D}/home/root
     #cp -f ${S}/examples/pxScene2d/src/waylandregistry.conf ${D}/home/root
   fi
   # utf8.h is causing xre build failure
   rm -f ${D}${includedir}/pxcore/utf*.h
   if [ "${@build_spark_rtremote(d)}" -eq '1' ]
   then
   	mkdir -p ${D}/etc
   	install -m 0644 "${WORKDIR}/rtremote.conf" "${D}/etc/"
   fi
}

pxcore_extra_clean() {
    rm -rf ${WORKDIR}/../../node_libnode
}
do_clean[prefuncs] += "pxcore_extra_clean"

FILES_${PN} += "/home/root"
FILES_${PN} += "${libdir}/*.so"
FILES_${PN} += "${bindir}/tests/*"
FILES_${PN}-dbg += "${bindir}/tests/.debug"
FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} += "dev-so staticdev"
INSANE_SKIP_${PN}_append_morty = " ldflags"

INSANE_SKIP_${PN}_append_morty = " ldflags"
DEBIAN_NOAUTONAME_${PN} = "1"

BBCLASSEXTEND = "native"

