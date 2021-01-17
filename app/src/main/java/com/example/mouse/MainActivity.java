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
    private UDPWrapper udpWrapper;
    private EditText editText;

    //keytype declarations
    private int prevLength;
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

        udpWrapper = ApplicationContainer.getUDPWrapper(getApplicationContext(), null);

        mousePad = findViewById(R.id.mouse);
        editText = findViewById(R.id.inputText);

        prevLength = 1;
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

//                if(s.length() == 0) {
//                    String data = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION + "," + KeyboardEvent.BKSP;
//                    udpWrapper.sendData(data.getBytes());
//                    addDummy();
//                    return;
//                }
//
//                getDiff(s, prevStr);
                KeyboardEvent.typeText(s, prevStr, udpWrapper, MainActivity.this);
            }
        });

        addDummy();

        MouseTouchListener listener = new MouseTouchListener(udpWrapper);
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
        winSwitch.setOnCheckedChangeListener(new SwitchListener(udpWrapper, KeyboardEvent.WIN));
        dragSwitch = findViewById(R.id.drag_switch);
        dragSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    String data = System.currentTimeMillis() + "," + PointerUtils.LEFT_BUTTON_PRESS;
                    udpWrapper.sendData(data.getBytes());
                } else {
                    String data = System.currentTimeMillis() + "," + PointerUtils.LEFT_BUTTON_RELEASE;
                    udpWrapper.sendData(data.getBytes());
                }
            }
        });
        shiftSwitch = findViewById(R.id.shift_switch);
        shiftSwitch.setOnCheckedChangeListener(new SwitchListener(udpWrapper, KeyboardEvent.SHIFT));
        altSwitch = findViewById(R.id.alt_switch);
        altSwitch.setOnCheckedChangeListener(new SwitchListener(udpWrapper, KeyboardEvent.ALT));
        ctrlSwitch = findViewById(R.id.ctrl_switch);
        ctrlSwitch.setOnCheckedChangeListener(new SwitchListener(udpWrapper, KeyboardEvent.CTRL));
        fnSwitch = findViewById(R.id.fn_switch);
        fnSwitch.setOnCheckedChangeListener(new SwitchListener(udpWrapper, KeyboardEvent.FN));
    }

    private void initButtons() {
        tabButton = findViewById(R.id.tab_button);
        tabButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.TAB));
        upButton = findViewById(R.id.up_button);
        upButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.UP));
        downButton = findViewById(R.id.down_button);
        downButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.DOWN));
        leftButton = findViewById(R.id.left_button);
        leftButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.LEFT));
        rightButton = findViewById(R.id.right_button);
        rightButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.RIGHT));
        enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.ENTER));
        escapeButton = findViewById(R.id.esc_button);
        escapeButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.ESC));
        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.DELETE));
        insertButton = findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.INSERT));

        f1Button = findViewById(R.id.f1_button);
        f1Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F1));
        f2Button = findViewById(R.id.f2_button);
        f2Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F2));
        f3Button = findViewById(R.id.f3_button);
        f3Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F3));
        f4Button = findViewById(R.id.f4_button);
        f4Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F4));
        f5Button = findViewById(R.id.f5_button);
        f5Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F5));
        f6Button = findViewById(R.id.f6_button);
        f6Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F6));
        f7Button = findViewById(R.id.f7_button);
        f7Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F7));
        f8Button = findViewById(R.id.f8_button);
        f8Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F8));
        f9Button = findViewById(R.id.f9_button);
        f9Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F9));
        f10Button = findViewById(R.id.f10_button);
        f10Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F10));
        f11Button = findViewById(R.id.f11_button);
        f11Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F11));
        f12Button = findViewById(R.id.f12_button);
        f12Button.setOnClickListener(new ButtonClickListener(udpWrapper, KeyboardEvent.F12));
    }

    public void addDummy() {
        isDummy = true;
        prevLength = 1;
        prevStr = dummyChar;
        editText.setText("");
        editText.append(dummyChar);
    }

    public void getDiff(String s, String t) {
        String logData = System.currentTimeMillis() + "," + PointerUtils.LOG + "," + LogUtil.PREV_CUR_STR + ", \"" + s + "\",\"" + t +"\"";
        udpWrapper.sendData(logData.getBytes());
        int minLen = Math.min(s.length(), t.length());
        int i;
        int j;
         for(i = 0, j = 0; i < minLen; i++, j++) {
             if (s.charAt(i) != t.charAt(j)) {
                 break;
             }
         }

        while(j < t.length() ) {
            String data = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION + "," +  KeyboardEvent.BKSP;
            udpWrapper.sendData(data.getBytes());
            j++;
        }

        if(i != s.length()) {
//            if (i == 0)
//                i = 1;
            String diff = s.substring(i);

            char letter = diff.charAt(0);
            if (diff.length() == 1 && isLetterOrDigit(letter)) {
                String data;
                char capLetter = letter;
                if ((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z')) {
                    capLetter = Character.toUpperCase(letter);
                }
                if(letter >= 'A' && letter <= 'Z') {
                    data = System.currentTimeMillis() + "," + PointerUtils.KEY_PRESSED + "," + KeyboardEvent.SHIFT;
                    udpWrapper.sendData(data.getBytes());
                }
                data = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION + ","  + (int)capLetter;
               udpWrapper.sendData(data.getBytes());
                if(letter >= 'A' && letter <= 'Z') {
                    data = System.currentTimeMillis() + "," + PointerUtils.KEY_RELEASED + "," + KeyboardEvent.SHIFT;
                    udpWrapper.sendData(data.getBytes());
                }
            } else {
                String comps[] = diff.split(" ");
                String data;
                for (int a = 0; a < comps.length; a++) {
                    if (comps.length > 1 && a < comps.length - 1) {
                        data = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION + "," + KeyboardEvent.SPACE;
                        udpWrapper.sendData(data.getBytes());
                    }
                    data = System.currentTimeMillis() + "," + PointerUtils.TEXT_INPUT + "," + comps[a];
                    if (!comps[a].equals("")) {
                        udpWrapper.sendData(data.getBytes());
                    }
                }

                if (s.split(" ").length > 5) {
                    addDummy();
                    return;
                }

                if (diff.length() > 0 && diff.charAt(diff.length() - 1) == ' ') {
                    data = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION + "," + KeyboardEvent.SPACE;
                    udpWrapper.sendData(data.getBytes());
                    addDummy();
                    return;
                }
            }
        } else {
            if(s.length() == 0) {
                addDummy();
            }
        }
        prevLength = s.length();
        prevStr = s;
    }

    private boolean isLetterOrDigit(char letter) {
        if(letter >= '0' && letter <= '9')
            return true;
        if(letter >= 'A' && letter <= 'Z')
            return true;
        if(letter >= 'a' && letter <= 'z')
            return true;
        return false;
    }
}