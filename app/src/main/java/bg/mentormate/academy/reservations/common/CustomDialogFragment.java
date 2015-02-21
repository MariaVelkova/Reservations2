package bg.mentormate.academy.reservations.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import bg.mentormate.academy.reservations.R;

/**
 * Created by PC on 11.2.2015 г..
 */
public class CustomDialogFragment extends android.support.v4.app.DialogFragment {
    TextView text;
    Button positive;
    Button negative;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yes_no_dialog, container, false);
        getDialog().setTitle("Confirm action");
        text = (TextView) view.findViewById(R.id.dialogMessage);
        positive = (Button) view.findViewById(R.id.positiveButton);
        negative = (Button) view.findViewById(R.id.negativeButton);
        positive.setText(getArguments().getString("positive"));
        negative.setText(getArguments().getString("negative"));
        text.setText(getArguments().getString("message"));
        final String phoneCode = getArguments().getString("number");
        getActivity().getIntent().putExtra("phoneCode", phoneCode);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("phonenumber", phoneCode);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Constants.RESULT_CODE_YES, data);
            //    getTargetFragment().onActivityResult(getTargetRequestCode(), Constants.RESULT_CODE_YES, getActivity().getIntent());
                dismiss();
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Constants.RESULT_CODE_NO, getActivity().getIntent());
                dismiss();
            }
        });

        return view;
    }
}
