package com.example.map_realtime.GetMyLocationInBackGround;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class GetMyLocationWorker extends Worker {

    public GetMyLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {

        Log.e("doWork", "doWork");
        Intent intent = new Intent(getApplicationContext(), GetMyLocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(intent);
        } else {
            getApplicationContext().startService(intent);
        }
        return Result.success();
    }

}

