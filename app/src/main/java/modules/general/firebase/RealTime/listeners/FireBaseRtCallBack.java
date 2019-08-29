package modules.general.firebase.RealTime.listeners;

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public    class FireBaseRtCallBack

  {
      private static FireBaseRtCallBack ourInstance;
      private static Context context;
      private IFireBaseRtCallBack iFireBaseRtCallBack ;



      public static FireBaseRtCallBack getInstance(Context context) {
          FireBaseRtCallBack.context=context;
          if (ourInstance == null)
              ourInstance = new FireBaseRtCallBack(context);
          return ourInstance;
      }

      private FireBaseRtCallBack(final Context context ) {
          FireBaseRtCallBack.context=context;
      }


      public void setiFireBaseRtCallBack(IFireBaseRtCallBack iFireBaseRtCallBack) {
          this.iFireBaseRtCallBack = iFireBaseRtCallBack;
      }




      public ValueEventListener valueEventListener = new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              iFireBaseRtCallBack.onDataChange(dataSnapshot);
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
              iFireBaseRtCallBack.onCancelled(databaseError);
          }
      };



      public ChildEventListener childEventListener = new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              iFireBaseRtCallBack.onChildAdded(dataSnapshot,s);

          }
          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {
              iFireBaseRtCallBack.onChildChanged(dataSnapshot,s);

          }
          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {
              iFireBaseRtCallBack.onChildRemoved(dataSnapshot);

          }
          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {
              iFireBaseRtCallBack.onChildMoved(dataSnapshot,s);
          }
          @Override
          public void onCancelled(DatabaseError databaseError) {
              iFireBaseRtCallBack.onCancelled(databaseError);
          }


      };


      public  void attachDatabaseReadListener(DatabaseReference databaseReference) {
          databaseReference.addChildEventListener(childEventListener);
      }

      public  void detachDatabaseReadListener(DatabaseReference databaseReference) {
          databaseReference.removeEventListener(childEventListener);
      }




}
