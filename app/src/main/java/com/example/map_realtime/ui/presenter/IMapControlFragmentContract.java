package com.example.map_realtime.ui.presenter;

import com.google.firebase.database.DataSnapshot;
import com.mvp_base.Base;


/**
 * Created by Net22 on 11/13/2017.
 */

public interface IMapControlFragmentContract {

    public interface IMapControlFragmentContractView {
    }

    public interface IMapControlFragemntContractPresenter extends Base.IPresenter {
        void attachChildListener( ) ;
        void removeChildListener( ) ;
         void userAdded(DataSnapshot singleSnapshot) ;
        void userChanged(DataSnapshot singleSnapshot) ;
        void userDeleted(DataSnapshot singleSnapshot) ;

    }
}
