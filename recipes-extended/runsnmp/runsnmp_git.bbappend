DEPENDS += "rmfpodmgr rmfpodserver"
CXXFLAGS += " \
        -I${STAGING_INCDIR}/rdk/podmgr \
"
export POD_LIBS="-lpodserver"

