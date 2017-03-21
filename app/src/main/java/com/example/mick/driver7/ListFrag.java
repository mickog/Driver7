package com.example.mick.driver7;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class ListFrag extends Fragment {

    public ListFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        PassDetailsActvity activity = (PassDetailsActvity) getActivity();
//        String myDataFromActivity = activity.getMyData();

        ArrayList<String> x =((MapA)getActivity()).getMyData();
        View view = inflater.inflate(R.layout.fragment_driver_list, container, false);

            String[] namesArr = new String[x.size()];
            namesArr = x.toArray(namesArr);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, Arrays.asList(namesArr));

            ListView list = (ListView) view.findViewById(R.id.listView);
            list.setAdapter(adapter);

            list.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> list, View row, int index, long rowID) {
                            // code to run when user clicks that item
                            // launch new Activity with holes details
                            ((MapA) getActivity()).setCity((String) list.getItemAtPosition(index));

                            ((MapA) getActivity()).showMap();


                        }
                    }
            );

        return view;
    }


}
