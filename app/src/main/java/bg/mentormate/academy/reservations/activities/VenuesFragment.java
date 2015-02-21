package bg.mentormate.academy.reservations.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.adapters.VenuesAdapter;
import bg.mentormate.academy.reservations.database.DBConstants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VenuesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VenuesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenuesFragment extends Fragment {
    private TextView mTextView;
    private ListView mListView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1 = "";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment VenuesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenuesFragment newInstance(String param1) {
        VenuesFragment fragment = new VenuesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public VenuesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_venues, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTextView = (TextView) getActivity().findViewById(R.id.text);
        mListView = (ListView) getActivity().findViewById(R.id.list);
        showResults(mParam1);
        Log.d("QUERY IS ", mParam1);
    }


    /**
     * Searches the dictionary and displays results for the given query.
     * @param query The search query
     */

    private void showResults(String query) {
        Log.d("QUERY IS ", query);
        Log.d("MainActivity", "showResults");
        Cursor cursor;
        String emptyString = new String ("");
        if (emptyString.equals(query)) {
            cursor = getActivity().getContentResolver().query(DBConstants.CONTENT_URI_VENUES, null, null, null, DBConstants.DB_TABLE_VENUES_NAME);
        } else {
            cursor = getActivity().getContentResolver().query(DBConstants.CONTENT_URI_VENUES, null, DBConstants.DB_TABLE_VENUES_NAME + " LIKE ?", new String[]{"%" + query + "%"}, DBConstants.DB_TABLE_VENUES_NAME);
        }
        if (!cursor.moveToFirst()) {
            Log.d("showResults", "There are no results");
            // There are no results
            mTextView.setText(getString(R.string.no_results, new Object[] {query}));
        } else {

            // Display the number of results
            int count = cursor.getCount();
            String countString;
            if (emptyString.equals(query)) {
                countString = getResources().getQuantityString(R.plurals.venues_list,
                        count,new Object[]{count});
            } else {
                countString = getResources().getQuantityString(R.plurals.search_results,
                        count, new Object[]{count, query});
            }
            mTextView.setText(countString);
            Log.d("showResults", countString);


            VenuesAdapter adapter = new VenuesAdapter(getActivity(), cursor);

//            ArrayList<String> queryResults = new ArrayList<String>();
//            String[] resultValuesArray = new String[count];
//            int i = 0;
//            do {
//                Log.d("VALUE",cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_NAME)));
//                resultValuesArray[i] = cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_NAME));
//                i++;
//            } while(cursor.moveToNext());
//
//            //resultValuesArray = resultValues.toArray(resultValuesArray);
//            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, resultValuesArray);
            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            // Define the on-click listener for the list items
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open VenueDetailActivity with a specific word Uri
                    Intent wordIntent = new Intent(getActivity().getApplicationContext(), VenueDetailActivity.class);
                    Uri data = Uri.withAppendedPath(DBConstants.CONTENT_URI_VENUES,
                            String.valueOf(id));
                    Log.d("ID1",String.valueOf(id));
                    Log.d("ID",String.valueOf(parent.getSelectedItemId()));
                    Log.d("URI", data.toString());
                    wordIntent.setData(data);
                    startActivity(wordIntent);
                }
            });

        }
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
