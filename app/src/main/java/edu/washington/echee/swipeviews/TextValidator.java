package edu.washington.echee.swipeviews;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eric on 3/5/15.
 */
public abstract class TextValidator implements TextWatcher{
    private TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public static boolean isNotEmpty(ArrayList<TextView> textViews) {
        boolean result = false;

        for (int i = 0; i < textViews.size(); i++) {
            if (textViews.get(i).getText().toString().trim().length() > 0) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isNotEmpty(TextView textView) {
        return textView.getText().toString().trim().length() > 0;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmail(TextView textView) {

        String emailStr = textView.getText().toString().trim();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static boolean isSame(TextView textView1, TextView textView2) {
        return textView1.getText().toString().equals(textView2.getText().toString());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textView, text);
    }

    public abstract void validate(TextView textView, String text);
}
