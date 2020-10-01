package org.androidtown.modifiedui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;


public class TextToSpeechActivity extends Activity implements TextToSpeechListener {
    private static final String TAG = "TextToSpeechActivity";

    private TextToSpeechClient ttsClient;

    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());

        mStatus = (TextView) findViewById(R.id.textViewStatus);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ImageButton buttonStartStop = (ImageButton) findViewById(R.id.imageButtonStart);
        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttsClient != null && ttsClient.isPlaying()) {
                    ttsClient.stop();
                    return;
                }

                EditText editText1 = (EditText) findViewById(R.id.editTextTTS);
                String strText = "대체 왜 안되냐구 왜왜왜왜왜";
                // String strText = editText1.getText().toString();

                String speechMode;
                speechMode = TextToSpeechClient.NEWTONE_TALK_1;

                String voiceType;
                voiceType = TextToSpeechClient.VOICE_WOMAN_READ_CALM;

                double speechSpeed;
                RadioGroup radioSpeed = (RadioGroup)findViewById(R.id.speedRadioGroup);
                switch(radioSpeed.getCheckedRadioButtonId()) {
                    case R.id.speed05x:
                        speechSpeed = 0.5D;
                        break;
                    case R.id.speed20x:
                        speechSpeed = 2.0D;
                        break;
                    case R.id.speed10x:
                    default:
                        speechSpeed = 1.0D;
                        break;
                }

//                ttsClient = new TextToSpeechClient.Builder()
//                        .setApiKey(SpeechSampleActivity.APIKEY)
//                        .setSpeechMode(speechMode)
//                        .setSpeechSpeed(speechSpeed)
//                        .setSpeechVoice(voiceType)
//                        .setListener(TextToSpeechActivity.this)
//                        .build();

                ttsClient = new TextToSpeechClient.Builder()
                        .setSpeechMode(speechMode)
                        .setSpeechSpeed(speechSpeed)
                        .setSpeechVoice(voiceType)
                        .setListener(TextToSpeechActivity.this)
                        .build();

                if (ttsClient.play(strText)) {
                    mStatus.setText("playTTS");
                }
            }
        });
    }

    private void handleError(int errorCode) {
        String errorText;
        switch (errorCode) {
            case TextToSpeechClient.ERROR_NETWORK:
                errorText = "네트워크 오류";
                break;
            case TextToSpeechClient.ERROR_NETWORK_TIMEOUT:
                errorText = "네트워크 지연";
                break;
            case TextToSpeechClient.ERROR_CLIENT_INETRNAL:
                errorText = "음성합성 클라이언트 내부 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_INTERNAL:
                errorText = "음성합성 서버 내부 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_TIMEOUT:
                errorText = "음성합성 서버 최대 접속시간 초과";
                break;
            case TextToSpeechClient.ERROR_SERVER_AUTHENTICATION:
                errorText = "음성합성 인증 실패";
                break;
            case TextToSpeechClient.ERROR_SERVER_SPEECH_TEXT_BAD:
                errorText = "음성합성 텍스트 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_SPEECH_TEXT_EXCESS:
                errorText = "음성합성 텍스트 허용 길이 초과";
                break;
            case TextToSpeechClient.ERROR_SERVER_UNSUPPORTED_SERVICE:
                errorText = "음성합성 서비스 모드 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_ALLOWED_REQUESTS_EXCESS:
                errorText = "허용 횟수 초과";
                break;
            default:
                errorText = "정의하지 않은 오류";
                break;
        }

        final String statusMessage = errorText + " (" + errorCode + ")";

        Log.i(TAG, statusMessage);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setText(statusMessage);
            }
        });
    }

    @Override
    public void onError(int code, String message) {
        handleError(code);

        ttsClient = null;
    }

    @Override
    public void onFinished() {
        int intSentSize = ttsClient.getSentDataSize();
        int intRecvSize = ttsClient.getReceivedDataSize();

        final String strInacctiveText = "onFinished() SentSize : " + intSentSize + " RecvSize : " + intRecvSize;

        Log.i(TAG, strInacctiveText);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setText(strInacctiveText);
            }
        });

        ttsClient = null;
    }
}

