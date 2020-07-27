package com.aditya.chatit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPassword_Bottom_dialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forgot_password_dialog,container,false);
        Button send_email = v.findViewById(R.id.send_password_reset_btn);
        final TextInputEditText email = v.findViewById(R.id.forgot_password_page_email);

        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = email.getText().toString();
                mListener.onButtonClicked(user_email);
                dismiss();
            }
        });
        return v;
    }

    public interface BottomSheetListener
    {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+"must implement BottomSheetListener");
        }

    }
}
