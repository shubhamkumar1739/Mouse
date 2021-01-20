package com.example.mouse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.KeyUtils.KeyboardEvent;
import com.example.mouse.KeyUtils.LogUtil;
import com.example.mouse.Listeners.ButtonClickListener;
import com.example.mouse.Listeners.MouseTouchListener;
import com.example.mouse.Listeners.SwitchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements KeyEvent.Callback {

    private ImageView mousePad;
    private Button keyboardButton;
    private NetworkManager networkManager;
    private EditText editText;

    //keytype declarations
    private boolean isDummy;

    public String getPrevStr() {
        return prevStr;
    }

    public void setPrevStr(String prevStr) {
        this.prevStr = prevStr;
    }

    private String prevStr;
    private String dummyChar = " ";

    private Button escapeButton, upButton, downButton, leftButton, rightButton, tabButton, enterButton, deleteButton, insertButton;
    private Button f1Button, f2Button, f3Button, f4Button, f5Button, f6Button, f7Button, f8Button, f9Button, f10Button, f11Button, f12Button;

    private Switch winSwitch, dragSwitch, shiftSwitch, altSwitch, ctrlSwitch, fnSwitch;

    private FloatingActionButton upDrawer, sideDrawer;
    private View buttonContainer, fnContainer, mousePadContainer;
    private boolean sideDrawerVisible, upDrawerVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkManager = ApplicationContainer.getNetworkManager(getApplicationContext(), null);

        mousePad = findViewById(R.id.mouse);
        editText = findViewById(R.id.inputText);

        prevStr = dummyChar;

        initButtons();
        initSwitches();
        initFabs();

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    v.requestFocus();
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if(text != null && text.length() > 0) {
                    editText.setSelection(text.length());
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edt) {
                if(isDummy) {
                    isDummy = false;
                    return;
                }

                String s = edt.toString();

                KeyboardEvent.typeText(s, prevStr, networkManager, MainActivity.this);
            }
        });

        addDummy();

        MouseTouchListener listener = new MouseTouchListener(networkManager, MainActivity.this);
        mousePad.setOnTouchListener(listener);
    }

    private void initFabs() {
        sideDrawerVisible = true;
        upDrawerVisible = true;

        upDrawer = findViewById(R.id.up_drawer);
        sideDrawer = findViewById(R.id.side_drawer);

        buttonContainer = findViewById(R.id.button_container);
        fnContainer = findViewById(R.id.fn_container);
        mousePadContainer = findViewById(R.id.button_box);

        sideDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sideDrawerVisible) {
                    fnContainer.setVisibility(View.GONE);
                    sideDrawer.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left));
                    mousePad.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            5));
                } else {
                    fnContainer.setVisibility(View.VISIBLE);
                    sideDrawer.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right));
                    mousePad.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3));
                }
                sideDrawerVisible = !sideDrawerVisible;
            }
        });

        upDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upDrawerVisible) {
                    buttonContainer.setVisibility(View.GONE);
                    upDrawer.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    mousePadContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 9.0f));
                } else {
                    buttonContainer.setVisibility(View.VISIBLE);
                    upDrawer.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    mousePadContainer.setLayoutParams(new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 0, 6.0f));
                }
                upDrawerVisible = !upDrawerVisible;
            }
        });
    }

    private void initSwitches() {
        winSwitch = findViewById(R.id.win_switch);
        winSwitch.setOnCheckedChangeListener(new SwitchListener(networkManager, KeyboardEvent.WIN, MainActivity.this));
        dragSwitch = findViewById(R.id.drag_switch);
        dragSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    String data = System.currentTimeMillis() + "," + PointerUtils.LEFT_BUTTON_PRESS;
                    networkManager.sendData(data.getBytes(), NetworkManager.UDP_OPTION);
                } else {
                    String data = System.currentTimeMillis() + "," + PointerUtils.LEFT_BUTTON_RELEASE;
                    networkManager.sendData(data.getBytes(), NetworkManager.UDP_OPTION);
                }
            }
        });
        shiftSwitch = findViewById(R.id.shift_switch);
        shiftSwitch.setOnCheckedChangeListener(new SwitchListener(networkManager, KeyboardEvent.SHIFT, MainActivity.this));
        altSwitch = findViewById(R.id.alt_switch);
        altSwitch.setOnCheckedChangeListener(new SwitchListener(networkManager, KeyboardEvent.ALT, MainActivity.this));
        ctrlSwitch = findViewById(R.id.ctrl_switch);
        ctrlSwitch.setOnCheckedChangeListener(new SwitchListener(networkManager, KeyboardEvent.CTRL, MainActivity.this));
        fnSwitch = findViewById(R.id.fn_switch);
        fnSwitch.setOnCheckedChangeListener(new SwitchListener(networkManager, KeyboardEvent.FN, MainActivity.this));
    }

    private void initButtons() {
        tabButton = findViewById(R.id.tab_button);
        tabButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.TAB, MainActivity.this));
        upButton = findViewById(R.id.up_button);
        upButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.UP, MainActivity.this));
        downButton = findViewById(R.id.down_button);
        downButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.DOWN, MainActivity.this));
        leftButton = findViewById(R.id.left_button);
        leftButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.LEFT, MainActivity.this));
        rightButton = findViewById(R.id.right_button);
        rightButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.RIGHT, MainActivity.this));
        enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.ENTER, MainActivity.this));
        escapeButton = findViewById(R.id.esc_button);
        escapeButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.ESC, MainActivity.this));
        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.DELETE, MainActivity.this));
        insertButton = findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.INSERT, MainActivity.this));

        f1Button = findViewById(R.id.f1_button);
        f1Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F1, MainActivity.this));
        f2Button = findViewById(R.id.f2_button);
        f2Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F2, MainActivity.this));
        f3Button = findViewById(R.id.f3_button);
        f3Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F3, MainActivity.this));
        f4Button = findViewById(R.id.f4_button);
        f4Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F4, MainActivity.this));
        f5Button = findViewById(R.id.f5_button);
        f5Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F5, MainActivity.this));
        f6Button = findViewById(R.id.f6_button);
        f6Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F6, MainActivity.this));
        f7Button = findViewById(R.id.f7_button);
        f7Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F7, MainActivity.this));
        f8Button = findViewById(R.id.f8_button);
        f8Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F8, MainActivity.this));
        f9Button = findViewById(R.id.f9_button);
        f9Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F9, MainActivity.this));
        f10Button = findViewById(R.id.f10_button);
        f10Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F10, MainActivity.this));
        f11Button = findViewById(R.id.f11_button);
        f11Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F11, MainActivity.this));
        f12Button = findViewById(R.id.f12_button);
        f12Button.setOnClickListener(new ButtonClickListener(networkManager, KeyboardEvent.F12, MainActivity.this));
    }

    public void addDummy() {
        isDummy = true;
        prevStr = dummyChar;
        editText.setText("");
        editText.append(dummyChar);
    }
}