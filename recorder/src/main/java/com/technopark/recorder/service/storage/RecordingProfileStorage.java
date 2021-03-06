package com.technopark.recorder.service.storage;

import android.media.AudioFormat;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class RecordingProfileStorage {

    private RecordingProfileStorage(){}

    private static final Map<AudioQuality, RecordingProfile> PROFILES_MAP = createMap();


    //parameters common to all recording profiles
    private static final int SAMPLING_RATE = 44100;
    private static final int CHANNEL_IN_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    //using only aac encoding
    private static final String FILE_FORMAT = ".aac";

    public enum AudioQuality {
        LOW, STANDARD, HIGH
    }

    private static Map<AudioQuality, RecordingProfile> createMap() {
        Map<AudioQuality, RecordingProfile> profiles = new EnumMap<>(AudioQuality.class);
        profiles.put(AudioQuality.LOW,new RecordingProfile(SAMPLING_RATE,CHANNEL_IN_CONFIG,
                AUDIO_FORMAT,64000, FILE_FORMAT));
        profiles.put(AudioQuality.STANDARD,new RecordingProfile(SAMPLING_RATE,CHANNEL_IN_CONFIG,
                AUDIO_FORMAT,128000, FILE_FORMAT));
        profiles.put(AudioQuality.HIGH,new RecordingProfile(SAMPLING_RATE,CHANNEL_IN_CONFIG,
                AUDIO_FORMAT,256000, FILE_FORMAT));
        return Collections.unmodifiableMap(profiles);
    }

    public static RecordingProfile getRecordingProfile(AudioQuality quality){
        return PROFILES_MAP.get(quality);
    }
}
