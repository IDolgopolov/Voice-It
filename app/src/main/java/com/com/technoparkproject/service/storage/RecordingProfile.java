package com.com.technoparkproject.service.storage;

import com.com.technoparkproject.service.utils.AudioFormatUtils;



public class RecordingProfile {

    private final int mSamplingRate;
    private final int mConfigChannels;
    private final int mAudioFormat;
    private final int mBitRate;
    private final String mFileFormat;

    public RecordingProfile(int samplingRate, int configChannels,
                            int audioFormat, int bitRate,
                            String fileFormat) {
        mSamplingRate = samplingRate;
        mConfigChannels = configChannels;
        mAudioFormat = audioFormat;
        mBitRate = bitRate;
        mFileFormat = fileFormat;
    }


    public int getAudioFormat() {
        return mAudioFormat;
    }

    public int getConfigChannels() {
        return mConfigChannels;
    }

    public int getSamplingRate() {
        return mSamplingRate;
    }

    public int getChannelsCount(){
        return AudioFormatUtils.getChannelCount(mConfigChannels);
    }

    public int getBytesPerSample(){
        return AudioFormatUtils.getBytesPerSample(mAudioFormat);
    }

    public int getBitRate() {
        return mBitRate;
    }

    public int getFrameSize(){
        return getChannelsCount()*getBytesPerSample();
    }

    public String getFileFormat() {
        return mFileFormat;
    }
}
