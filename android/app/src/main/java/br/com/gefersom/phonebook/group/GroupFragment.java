package br.com.gefersom.phonebook.group;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {


    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //setHasOptionsMenu(true);
//        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
//
//        ListView listView = (ListView) rootView.findViewById(R.id.list);
//        listView.setAdapter( new GroupAdapter(getActivity(), DataBaseMock.getInstance().getGroupList()));
//        getActivity().setTitle(R.string.group_title);
//
//        return rootView;
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.group, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static class AddGroupFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //setHasOptionsMenu(true);
//            View rootView = inflater.inflate(R.layout.fragment_group, container, false);

//            ListView listView = (ListView) rootView.findViewById(R.id.list);
//            listView.setAdapter( new GroupAdapter(getActivity(), DataBaseMock.getInstance().getGroupList()));
//            getActivity().setTitle(R.string.group_title);
//
//            return rootView;
            return null;
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            //inflater.inflate(R.menu.group, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    };
}
