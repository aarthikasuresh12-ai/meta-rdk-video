PV = "${RDK_RELEASE}+git${SRCPV}"

S = "${WORKDIR}/git"

CFLAGS += "-DUSE_IARM_BUS -DRMF_STREAMER -DOOBSI_SUPPORT -DGLI -DUSE_TRM -DQAMSRC_PMTBUFFER_PROPERTY -DUSE_FRONTPANEL -DUSE_FPTEXT -DUSE_FPLED -I=${includedir}/rdk/iarmmgrs/sysmgr -I=${includedir}/rdk/iarmbus -I=${includedir}/openssl-0.9"

CXXFLAGS += "-DUSE_IARM_BUS -DRMF_STREAMER -DOOBSI_SUPPORT -DGLI -DUSE_TRM -DQAMSRC_PMTBUFFER_PROPERTY -DUSE_FRONTPANEL -DUSE_FPTEXT -DUSE_FPLED -I=${includedir}/rdk/iarmmgrs/sysmgr -I=${includedir}/rdk/iarmbus -I=${includedir}/openssl-0.9"

export RMF_BUILD_OS="RI_LinuxCanmore"
export POD_PLATFORM="INTEL_CANMORE"
export MFR_NAME="RDK_SIMULATOR"

PACKAGE_ARCH = "${MACHINE_ARCH}"

