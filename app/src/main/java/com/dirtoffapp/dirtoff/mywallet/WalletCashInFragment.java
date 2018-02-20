package com.dirtoffapp.dirtoff.mywallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dirtoffapp.dirtoff.R;

public class WalletCashInFragment extends Fragment {



    public WalletCashInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_cash_in, container, false);



        return view;
    }

}
