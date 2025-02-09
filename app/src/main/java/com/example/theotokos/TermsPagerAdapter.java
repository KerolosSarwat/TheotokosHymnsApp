package com.example.theotokos;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TermsPagerAdapter extends FragmentStateAdapter {
    private final Degree degree;

    public TermsPagerAdapter(FragmentActivity fa, Degree degree) {
        super(fa);
        this.degree = degree;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Term term;
        try {
            switch (position) {
                case 0:
                    term = degree.getFirstTerm();
                    break;
                case 1:
                    term = degree.getSecondTerm();
                    break;
                case 2:
                    term = degree.getThirdTerm();
                    break;
                default:
                    term = null;
            }
        }catch (NullPointerException ex){
         term = new Term(0,0,0,0,0);
        }

        return TermFragment.newInstance(term);
    }

    @Override
    public int getItemCount() {
        return 3; // Number of terms
    }
}