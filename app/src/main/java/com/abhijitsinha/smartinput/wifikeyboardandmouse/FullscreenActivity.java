package com.abhijitsinha.smartinput.wifikeyboardandmouse;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abhijitsinha.smartinput.wifikeyboardandmouse.communication.MessageSender;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.connector.ConnectionManager;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.connector.ConnectionManagerListener;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.controller.Command;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.controller.CommandType;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.controller.CommandValue;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.controller.impl.KeyValue;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.controller.impl.MoveValue;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.utils.CommandQueue;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.utils.GestureTap;
import com.abhijitsinha.smartinput.wifikeyboardandmouse.utils.Settings;

import java.util.Set;

import static android.view.MotionEvent.INVALID_POINTER_ID;


public class FullscreenActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, DialogInterface.OnClickListener, ConnectionManagerListener {

    private String TAG = FullscreenActivity.class.getCanonicalName();
    private EditText hostEditText;
    private EditText portEditText;
    private boolean connected;
    private Context context;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        this.context = this;
        detector = new GestureDetector(this, new GestureTap());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Button settings = findViewById(R.id.settings_button);
        settings.setOnClickListener(this);

        Button keyboardSwitch = findViewById(R.id.keyboard_toggle);
        keyboardSwitch.setOnClickListener(this);

        RelativeLayout touchscreen = findViewById(R.id.touch_screen);
        touchscreen.setOnTouchListener(this);

        MessageSender.getInstance().init();
        ConnectionManager.getInstanceOf().addConnectionManagerListener(this);
    }

    @Override
    public void onClick(View view) {
        String msg = String.format("click is happening on %s", view.getId());
        switch (view.getId()) {
            case R.id.keyboard_toggle: {
                toggleSoftKeyboard();
            }
            break;
            case R.id.settings_button: {
                AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings, null);
                hostEditText = dialogView.findViewById(R.id.button_host);
                portEditText = dialogView.findViewById(R.id.button_port);

                if (Settings.getInstanceOf().getHost() != null && !Settings.getInstanceOf().getHost().isEmpty()) {
                    hostEditText.setText(Settings.getInstanceOf().getHost());
                }
                if (Settings.getInstanceOf().getPort() != 0) {
                    portEditText.setText(String.format("%s", Settings.getInstanceOf().getPort()));
                }

                if (connected) {
                    builder.setNegativeButton("Disconnect", this);
                } else {
                    builder.setPositiveButton("Connect", this);
                }
                builder.setNeutralButton("Cancel", this);
                builder.setView(dialogView);
                builder.setCancelable(false);
                builder.show();
            }
            break;
        }
        Log.i(TAG, msg);
    }

    private void toggleSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (connected) {
            CommandQueue.getInstance().push(Command.of(CommandType.KEYBOARD_INPUT, KeyValue.of(event)));
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (connected) {
            detector.onTouchEvent(motionEvent);
        }

        return true;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.i(TAG, "I: " + i);
        switch (i) {
            case -1: {
                // positive
                String host = hostEditText.getText().toString();
                String portString = portEditText.getText().toString();
                if (host.isEmpty() || portString.isEmpty()) {
                    return;
                }
                int port = Integer.parseInt(portString);
                Settings.getInstanceOf().setHost(host);
                Settings.getInstanceOf().setPort(port);
                ConnectionManager.getInstanceOf().attemptConnection();
            }
            break;
            case -2: {
                // negative
                ConnectionManager.getInstanceOf().disconnect();
            }
            break;
        }
    }

    @Override
    public void onConnected() {
        connected = true;
        final String msg = "Connection established with host machine";
        Log.i(TAG, msg);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDisconnected() {
        connected = false;
        final String msg = "Connection disrupted";
        Log.e(TAG, msg);
//        ConnectionManager.getInstanceOf().attemptConnection();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnectionFailed() {
        connected = false;
        final String msg = "Could not establish connection with host machine";
        Log.e(TAG, msg);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
