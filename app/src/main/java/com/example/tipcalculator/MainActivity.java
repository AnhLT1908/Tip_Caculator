package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.text.NumberFormat;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();

    private double billAmount = 0.0;
    private double customPercent = 0.18;
    private double taxAmount = 0.0;
    private int numberOfPeople = 1;
    private TextView amountDisplayTextView, percentCustomTextView, tip15TextView, total15TextView,
            tipCustomTextView, totalCustomTextView, perPersonTextView;
    private EditText taxEditText, peopleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các TextView và EditText từ giao diện người dùng
        amountDisplayTextView = findViewById(R.id.amountDisplayTextView);
        percentCustomTextView = findViewById(R.id.percentCustomTextView);
        tip15TextView = findViewById(R.id.tip15TextView);
        total15TextView = findViewById(R.id.total15TextView);
        tipCustomTextView = findViewById(R.id.tipCustomTextView);
        totalCustomTextView = findViewById(R.id.totalCustomTextView);
        perPersonTextView = findViewById(R.id.perPersonTextView);
        taxEditText = findViewById(R.id.taxEditText);
        peopleEditText = findViewById(R.id.peopleEditText);

        // Cài đặt giá trị hiển thị ban đầu cho hóa đơn
        amountDisplayTextView.setText(currencyFormat.format(billAmount));
        updateStandard();
        updateCustom();

        // Thiết lập TextWatcher cho các trường nhập liệu
        EditText amountEditText = findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(amountEditTextWatcher);

        SeekBar customTipSeekBar = findViewById(R.id.customTipSeekBar);
        customTipSeekBar.setOnSeekBarChangeListener(customSeekBarListener);

        taxEditText.addTextChangedListener(taxEditTextWatcher);
        peopleEditText.addTextChangedListener(peopleEditTextWatcher);
    }

    // Hàm cập nhật tip và tổng cộng khi chọn mức 15% cố định
    private void updateStandard() {
        double effectiveAmount = billAmount + taxAmount; // Tổng tiền sau khi cộng thuế
        double fifteenPercentTip = effectiveAmount * 0.15;
        double fifteenPercentTotal = effectiveAmount + fifteenPercentTip;

        tip15TextView.setText(currencyFormat.format(fifteenPercentTip));
        total15TextView.setText(currencyFormat.format(fifteenPercentTotal));
        updatePerPerson(fifteenPercentTotal);
    }

    // Hàm cập nhật tip và tổng cộng khi chọn mức tip tùy chỉnh
    private void updateCustom() {
        percentCustomTextView.setText(percentFormat.format(customPercent));

        double effectiveAmount = billAmount + taxAmount; // Tổng tiền sau khi cộng thuế
        double customTip = effectiveAmount * customPercent;
        double customTotal = effectiveAmount + customTip;

        tipCustomTextView.setText(currencyFormat.format(customTip));
        totalCustomTextView.setText(currencyFormat.format(customTotal));
        updatePerPerson(customTotal);
    }

    // Hàm cập nhật tổng số tiền mỗi người phải trả nếu chia đều hóa đơn
    private void updatePerPerson(double totalAmount) {
        if (numberOfPeople > 0) {
            double perPersonAmount = totalAmount / numberOfPeople;
            perPersonTextView.setText(currencyFormat.format(perPersonAmount));
        }
    }

    // Xử lý thay đổi mức phần trăm tip từ SeekBar
    private OnSeekBarChangeListener customSeekBarListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            customPercent = progress / 100.0;
            updateCustom();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    // TextWatcher để theo dõi và cập nhật giá trị hóa đơn
    private TextWatcher amountEditTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                billAmount = Double.parseDouble(s.toString()) / 100.0;
            } catch (NumberFormatException e) {
                billAmount = 0.0;
            }
            amountDisplayTextView.setText(currencyFormat.format(billAmount));
            updateStandard();
            updateCustom();
        }
        @Override
        public void afterTextChanged(Editable s) { }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    };

    // TextWatcher để theo dõi và cập nhật giá trị thuế
    private TextWatcher taxEditTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                taxAmount = Double.parseDouble(s.toString()) / 100.0;
            } catch (NumberFormatException e) {
                taxAmount = 0.0;
            }
            updateStandard();
            updateCustom();
        }
        @Override
        public void afterTextChanged(Editable s) { }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    };

    // TextWatcher để theo dõi và cập nhật số lượng người trong nhóm
    private TextWatcher peopleEditTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                numberOfPeople = Integer.parseInt(s.toString());
            } catch (NumberFormatException e) {
                numberOfPeople = 1;
            }
            updateStandard();
            updateCustom();
        }
        @Override
        public void afterTextChanged(Editable s) { }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    };
}
