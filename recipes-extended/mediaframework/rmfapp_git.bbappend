EXTRA_OECONF_append_hybrid = " --enable-dvr"

RMFAPP_HYBRID_DEPENDS += " virtual/dvrmgr-hal dvrmgr"
CFLAGS_append_hybrid = " -I${STAGING_INCDIR}/rdk/podmgr"
CXXFLAGS_append_hybrid = " -I${STAGING_INCDIR}/rdk/podmgr"
