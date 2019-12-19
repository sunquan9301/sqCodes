package sunquan.com.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import androidx.core.app.ActivityCompat;

public final class PermissionUtil {
    private PermissionUtil() {
    }

    /**
     * 判断是是否有录音权限
     */
    public static boolean hasAudioPermission(Context context) {
        boolean hasPermission = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            return false;
        }
        //开始录制音频
        try {
            int bufferSizeInBytes = AudioRecord.getMinBufferSize(44100,
                    AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                    AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes);
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
            if (audioRecord.getRecordingState() == AudioRecord.STATE_UNINITIALIZED) {
                audioRecord.stop();
                audioRecord.release();
                return false;
            }
            audioRecord.stop();
            audioRecord.release();
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean hasModifyAudioPermission(Activity activity) {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS);
    }
}
