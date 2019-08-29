package com.example.map_realtime.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.map_realtime.R;
import com.example.map_realtime.ui.presenter.IMapControlActivityContract;
import com.example.map_realtime.ui.presenter.MapControlActivityPresenter;

import modules.general.model.shareddata.Prefs;
import modules.general.ui.parentview.ParentActivity;

import static com.example.map_realtime.Utils.Constants.FIREBASE_TOKEN_KEY;
import static com.example.map_realtime.Utils.Constants.FIREBASE_TOKEN_KEY_READY_EVENT;

public class MapControlActivity extends ParentActivity<MapControlActivityPresenter> implements

        IMapControlActivityContract.IMapControlActivityContractView {


    @Override
    public void configureUI() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        disableDrawerSwipe();
         getCsTitle().updateTitle(getString(R.string.app_name));
         if(!Prefs.getString(FIREBASE_TOKEN_KEY).isEmpty())
         {
             getMapControlActivityPresenter().openFragmentMap();
         }
         else
         {
             Toast.makeText(this,getString(R.string.firebase_token_not_ready),Toast.LENGTH_LONG).show();
         }
    }

    @Override
    public MapControlActivityPresenter injectDependencies() {
        return new MapControlActivityPresenter(this, this);
    }

    public MapControlActivityPresenter getMapControlActivityPresenter() {
        return ((MapControlActivityPresenter) this.getPresenter());
    }

    @Override
    protected void onResume() {
         LocalBroadcastManager.getInstance(this).registerReceiver(
                 mReceiver, new IntentFilter(FIREBASE_TOKEN_KEY_READY_EVENT));
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mReceiver);
        super.onPause();
    }

      private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
             if(intent.getAction().equals(FIREBASE_TOKEN_KEY_READY_EVENT))
            {
                Toast.makeText(MapControlActivity.this,getString(R.string.firebase_token_ready),Toast.LENGTH_LONG).show();
                getMapControlActivityPresenter().openFragmentMap();
            }
         }
    };

}


