package com.example.theotokos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_term#newInstance} factory method to
 * create an instance of this fragment.
 */public class TermFragment extends Fragment {
    private static final String ARG_TERM = "term";

    public static TermFragment newInstance(Term term) {
        TermFragment fragment = new TermFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TERM, term);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term, container, false);

        // Get the term object from arguments
        Term term = (Term) getArguments().getSerializable(ARG_TERM);

        // Display the term's degrees in your UI (e.g., TextViews)
        TextView agbyaTextView = view.findViewById(R.id.agbyaTextView);
        TextView copticTextView = view.findViewById(R.id.copticTextView);
        TextView hymnsTextView = view.findViewById(R.id.hymnsTextView);
        TextView taksTextView = view.findViewById(R.id.taksTextView);

        if (term != null) {
            agbyaTextView.setText("أجبية: " + term.getAgbya());
            copticTextView.setText("لغة قبطية: " + term.getCoptic());
            hymnsTextView.setText("ألحان: " + term.getHymns());
            taksTextView.setText("الطقس: " + term.getTaks());
        }

        return view;
    }
}