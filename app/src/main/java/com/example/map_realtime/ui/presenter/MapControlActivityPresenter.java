package com.example.map_realtime.ui.presenter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.map_realtime.ui.MapControlActivity;
import com.example.map_realtime.ui.MapControlFragment;


/**
 * Created by Net22 on 11/13/2017.
 */

public class MapControlActivityPresenter
        implements IMapControlActivityContract.IMapControlActivityContractPresenter {
    private final Context mContext;
    IMapControlActivityContract.IMapControlActivityContractView mView;

    public MapControlActivityPresenter(Context context, IMapControlActivityContract.IMapControlActivityContractView view) {
        mView = view;
        mContext = context;
    }



    @Override
    public void openFragmentMap() {
        Fragment fragment = new MapControlFragment();
        String fragmentTag = fragment.getClass().getName();
        FragmentManager manager = ((MapControlActivity) mContext).getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(((MapControlActivity) mContext).getContainerID(), fragment, fragmentTag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}