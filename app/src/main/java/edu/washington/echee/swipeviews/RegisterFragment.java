package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterFragment extends Fragment {
    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "password";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String name;
    private String email;
    private String username;
    private String password;
    private LoginRegisterActivity hostActivity;


    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        addValidationListeners(view);

        Button btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RegisterFragment", "Register button clicked");
                if (hasAllValidFields(view)) {
                    // todo write to file

                    hostActivity.showSwipe();
                }
            }
        });

        return view;
    }

    private void addValidationListeners(View view){
        EditText etName = (EditText) view.findViewById(R.id.etName_register);
        etName.addTextChangedListener(new TextValidator(etName) {
            @Override
            public void validate(TextView textView, String text) {
                name = setValidIcon(textView, true, TextValidator.isNotEmpty(textView));
            }
        });

        EditText etEmail = (EditText) view.findViewById(R.id.etEmail);
        etEmail.addTextChangedListener(new TextValidator(etEmail) {
            @Override
            public void validate(TextView textView, String text) {
                email = setValidIcon(textView, true,
                        (TextValidator.isNotEmpty(textView) && TextValidator.isEmail(textView)));
            }
        });


        EditText etUsername = (EditText) view.findViewById(R.id.etUsername_register);
        etUsername.addTextChangedListener(new TextValidator(etUsername) {
            @Override
            public void validate(TextView textView, String text) {
                username = setValidIcon(textView, true, TextValidator.isNotEmpty(textView));
            }
        });


        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword_register);
        final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.etPasswordConfirm_register);

        etPassword.addTextChangedListener(new TextValidator(etPassword) {
            @Override
            public void validate(TextView textView, String text) {
                password = setValidIcon(textView, false, TextValidator.isNotEmpty(textView));

                if (setValidIcon(etPasswordConfirm, false,
                        (TextValidator.isNotEmpty(etPasswordConfirm) && TextValidator.isSame(etPassword, etPasswordConfirm))) == null){
                    password = null;
                }
            }
        });


        etPasswordConfirm.addTextChangedListener(new TextValidator(etPasswordConfirm) {
            @Override
            public void validate(TextView textView, String text) {
                if (setValidIcon(textView, false,
                        (TextValidator.isNotEmpty(textView) && TextValidator.isSame(etPassword, etPasswordConfirm))) != null){
                    password = etPassword.getText().toString();
                }
            }
        });

    }


    public boolean hasAllValidFields(View view){
        Log.i("RegisterFragment", "validatingFields");

//        EditText etName = (EditText) view.findViewById(R.id.etName_register);
//        name = setValidIcon(etName, true, TextValidator.isNotEmpty(etName));
//
//        EditText etEmail = (EditText) view.findViewById(R.id.etEmail);
//        email = setValidIcon(etEmail, true,
//                (TextValidator.isNotEmpty(etEmail) && TextValidator.isEmail(etEmail)));
//
//        EditText etUsername = (EditText) view.findViewById(R.id.etUsername_register);
//        username = setValidIcon(etUsername, true, TextValidator.isNotEmpty(etUsername));
//
//        EditText etPassword = (EditText) view.findViewById(R.id.etPassword_register);
//        password = setValidIcon(etPassword, false, TextValidator.isNotEmpty(etPassword));
//
//        EditText etPasswordConfirm = (EditText) view.findViewById(R.id.etPasswordConfirm_register);
//        String passwordConfirm = setValidIcon(etPasswordConfirm, false,
//                (TextValidator.isNotEmpty(etPasswordConfirm) && TextValidator.isSame(etPassword, etPasswordConfirm)));
//
//        if (passwordConfirm == null) {
//            password = null;
//        }

        return (name != null && email != null && username != null && password != null);
    }

    private String setValidIcon(TextView editText, boolean trim, boolean test) {

        String result = null;
        if (test) {
            Log.i("RegisterFragment", editText.getHint().toString() + " is valid");
            Drawable greenCheck = getResources().getDrawable(R.drawable.green_check);
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, greenCheck, null);

            result = editText.toString();

            if (trim) {
                result.trim();
            }
        } else {
            Log.i("RegisterFragment", editText.getHint().toString() + " is NOT valid");

            Drawable greyCheck = getResources().getDrawable(R.drawable.grey_check);
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, greyCheck, null);
        }

        return result;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostActivity = (LoginRegisterActivity) activity; // TODO Change Activity Type

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
