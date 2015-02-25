package bg.mentormate.academy.reservations.activities.user_account;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.PostRequest;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserAccountFragment extends Fragment {

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    SessionData sessionData = SessionData.getInstance();
    User user = sessionData.getUser();
    int userId = 0;
    EditText userFirstName;
    EditText userLastName;
    EditText userEmail;
    EditText userPhone;
    Spinner userCity;
    EditText userPassword;
    EditText userPassword2;
    ImageView userAvatar;

    byte[] avatarByteArray = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserAccountFragment newInstance(String param1, String param2) {
        UserAccountFragment fragment = new UserAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_account, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int p = 0;
        userId = user.getId();

        final Activity activity = getActivity();
        String userFirstNameValue = "";
        String userLastNameValue = "";
        String userEmailValue = "";
        String userPhoneValue = "";
        String userCityValue = "";
        String userAvatarValue = "";
        String emptyString = "";
        String userPasswordValue = "";
        String userPassword2Value = "";

        userFirstNameValue = user.getFirstName();
        userLastNameValue = user.getLastName();
        userEmailValue = user.getEmail();
        userPhoneValue = user.getPhone();
        userCityValue = user.getCity();

//        if (bitmap != null)
//        {
//            bitmap.recycle();
//        }


        userAvatar = (ImageView) activity.findViewById(R.id.userAvatar);
        userAvatarValue = user.getAvatar();
        userAvatarValue = userAvatarValue.trim();

        if (!Validator.isEmpty(userAvatarValue)) {
            byte[] userAvatarBytes = Base64.decode(userAvatarValue, Base64.URL_SAFE);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userAvatarBytes);
            BitmapDrawable avatar  = new BitmapDrawable(byteArrayInputStream);

            userAvatar.setImageDrawable(avatar);
        }

        userFirstName = (EditText) activity.findViewById(R.id.userFirstName);
        userFirstName.setText(userFirstNameValue);
        userLastName = (EditText) activity.findViewById(R.id.userLastName);
        userLastName.setText(userLastNameValue);
        userEmail = (EditText) activity.findViewById(R.id.userEmail);
        userEmail.setText(userEmailValue);
        userPhone = (EditText) activity.findViewById(R.id.userPhone);
        userPhone.setText(userPhoneValue);
        userCity = (Spinner) activity.findViewById(R.id.userCity);
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        List<String> list = new ArrayList<String>();
//        ArrayList<City> cities = SessionData.getInstance().getCities();
//        for (int i = 0; i < cities.size(); i++) {
//            list.add(cities.get(i).getName());
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        userCity.setAdapter(dataAdapter);
        ArrayAdapter<CharSequence> cityAdapter;
        cityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cities_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userCity.setAdapter(cityAdapter);
        if (!emptyString.equals(userCityValue)) {
            int spinnerPostion = cityAdapter.getPosition(userCityValue);
            userCity.setSelection(spinnerPostion);
            spinnerPostion = 0;
        }
        userPassword = (EditText) activity.findViewById(R.id.userPassword);
        userPassword2 = (EditText) activity.findViewById(R.id.userPassword2);

        Button saveBtn = (Button) activity.findViewById(R.id.updateUserBtn);
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String emptyString = new String("");
                        ArrayList<String> errors = new ArrayList<String>();

                        String userFirstNameValue = userFirstName.getText().toString();
                        String userLastNameValue = userLastName.getText().toString();
                        String userPasswordValue = userPassword.getText().toString();
                        String userPassword2Value = userPassword2.getText().toString();
                        String userPhoneValue = userPhone.getText().toString();
                        String userEmailValue = userEmail.getText().toString();
                        String userCityValue = userCity.getSelectedItem().toString();
                        int userTypeValue = 1;

                        if (emptyString.equals(userEmailValue)) {
                            errors.add("Empty Email");
                        } else if (!Validator.validateEmailAddress(userEmailValue)) {
                            errors.add("Invalid Email");
                        }
                        if (!emptyString.equals(userPasswordValue) && !userPassword2Value.equals(userPasswordValue)) {
                            errors.add("Passwords do not match");
                        }
                        if (emptyString.equals(userFirstNameValue)) {
                            errors.add("Empty First Name");
                        }
                        if (emptyString.equals(userLastNameValue)) {
                            errors.add("Empty Last Name");
                        }
                        if (emptyString.equals(userPhoneValue)) {
                            errors.add("Empty Phone");
                        } else if(!Validator.validateMobileNumber(userPhoneValue)) {
                            errors.add("Invalid Phone. Phone number should be 10 digits");
                        }
                        if (errors.size() > 0) {
                            String validationString = TextUtils.join("\n", errors);
                            Toast.makeText(activity, validationString, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("REGISTRATION", "VALID");
                            if (!Validator.isEmpty(userPasswordValue)) {
                                userPasswordValue = Validator.md5(userPasswordValue);
                            }
                            String encodedImage = "";
                            if (avatarByteArray != null) {
                                encodedImage = Base64.encodeToString(avatarByteArray, Base64.DEFAULT);
                                String yourText = new String(avatarByteArray, UTF8_CHARSET);
                            }
                            PostRequest registrationTask = null;
                            try {
                                registrationTask = new PostRequest(getActivity(),userId, userEmailValue, userPasswordValue, Integer.toString(userTypeValue) , userFirstNameValue, userLastNameValue, userCityValue, userPhoneValue, encodedImage);
                            } catch (NetworkErrorException e) {
                                e.printStackTrace();
                            }
                            String result = "";
                            if (registrationTask != null) {
                                try {
                                    result = registrationTask.execute().get();
                                    int i = 0;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (Validator.isEmpty(result)) {
                                Toast.makeText(activity, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONObject resultJSON = null;
                                try {
                                    resultJSON = new JSONObject(result);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (resultJSON != null) {
                                    int code = 0;
                                    String message = "";
                                    JSONObject data = null;
                                    try {
                                        code = resultJSON.getInt("code");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (code == 1) {
                                        user.setFirstName(userFirstNameValue);
                                        user.setLastName(userLastNameValue);
                                        user.setEmail(userEmailValue);
                                        user.setPhone(userPhoneValue);
                                        user.setCity(userCityValue);
                                        user.setAvatar(encodedImage);
                                        if (!Validator.isEmpty(userPasswordValue)) {
                                            user.setPassword(userPasswordValue);
                                        }
                                        sessionData.setUser(user);

                                    }

                                    try {
                                        message = resultJSON.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (!Validator.isEmpty(message)) {
                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(activity, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }
                }
        );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


}
