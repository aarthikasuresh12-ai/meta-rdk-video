FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

#Enforce to use openssl_1.0.2o version dunfell build
SRC_URI_append_class-target_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', ' file://Use_openssl_1_0_2o_for_curl_7_69_1.patch', '', d)}"
EXTRA_OECONF_append_class-target_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', ' --enable-openssl10', '', d)}"
DEPENDS_append_class-target_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', ' openssl-1.0.2o', '', d)}"
DEPENDS_remove_class-target_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'openssl', '', d)}"
CFLAGS_append_class-target_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', ' -I${STAGING_INCDIR}/openssl-1.0.2o', '', d)}"
LDFLAGS_append_class-target_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'wifi', ' -L${STAGING_LIBDIR}/openssl-1.0.2o -lcrypto-1.0.2o -lssl-1.0.2o', '', d)}"

