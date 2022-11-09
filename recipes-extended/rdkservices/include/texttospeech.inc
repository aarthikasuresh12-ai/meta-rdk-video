# Provides the TextToSpeech plugin settings for WPEFramwork

TEXTTOSPEECH_ENDPOINT ?= " "
TEXTTOSPEECH_SECURE_ENDPOINT ?= " "
TEXTTOSPEECH_LANGUAGE ?= "en-US"
TEXTTOSPEECH_VOLUME ?= "100"
TEXTTOSPEECH_RATE ?= "50"
TEXTTOSPEECH_VOICE_FOR_EN ?= " "
TEXTTOSPEECH_VOICE_FOR_ES ?= " "
TEXTTOSPEECH_VOICE_FOR_FR ?= " "
TEXTTOSPEECH_VOICE_FOR_GB ?= " "
TEXTTOSPEECH_VOICE_FOR_DE ?= " "
TEXTTOSPEECH_VOICE_FOR_IT ?= " "

# ----------------------------------------------------------------------------

PACKAGECONFIG[texttospeech] = "-DPLUGIN_TEXTTOSPEECH=ON \
                               -DPLUGIN_TEXTTOSPEECH_ENDPOINT="${TEXTTOSPEECH_ENDPOINT}" \
                               -DPLUGIN_TEXTTOSPEECH_SECURE_ENDPOINT="${TEXTTOSPEECH_SECURE_ENDPOINT}" \
                               -DPLUGIN_TEXTTOSPEECH_LANGUAGE="${TEXTTOSPEECH_LANGUAGE}" \
                               -DPLUGIN_TEXTTOSPEECH_VOLUME="${TEXTTOSPEECH_VOLUME}" \
                               -DPLUGIN_TEXTTOSPEECH_RATE="${TEXTTOSPEECH_RATE}" \
                               -DPLUGIN_TEXTTOSPEECH_VOICE_FOR_EN="${TEXTTOSPEECH_VOICE_FOR_EN}" \
                               -DPLUGIN_TEXTTOSPEECH_VOICE_FOR_ES="${TEXTTOSPEECH_VOICE_FOR_ES}" \
                               -DPLUGIN_TEXTTOSPEECH_VOICE_FOR_FR="${TEXTTOSPEECH_VOICE_FOR_FR}" \
                               -DPLUGIN_TEXTTOSPEECH_VOICE_FOR_GB="${TEXTTOSPEECH_VOICE_FOR_GB}" \
                               -DPLUGIN_TEXTTOSPEECH_VOICE_FOR_DE="${TEXTTOSPEECH_VOICE_FOR_DE}" \
                               -DPLUGIN_TEXTTOSPEECH_VOICE_FOR_IT="${TEXTTOSPEECH_VOICE_FOR_IT}" \
                               , -DPLUGIN_TEXTTOSPEECH=OFF,"

# ----------------------------------------------------------------------------
