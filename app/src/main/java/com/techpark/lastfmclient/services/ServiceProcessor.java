package com.techpark.lastfmclient.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.techpark.lastfmclient.providers.IProvider;
import com.techpark.lastfmclient.providers.UsersProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Andrew Gov on 08.11.14
 */

/**
 * Service for making async calls to entities providers
 */
public class ServiceProcessor extends Service {

    private static final String TAG = ServiceProcessor.class.getSimpleName();

    public static final String PROVIDER = "provider";
    public static final String METHOD = "method";

    private int lastStartId;
    private final HashMap<String, ServiceTask> mTasks = new HashMap<>();


    /* Identifier for each supported provider. */
    public static class Providers {
        public static final int USERS_PROVIDER = 1;
        public static final int RECENT_TRACKS_PROVIDER = 2;
    }

    private IProvider getProvider(int providerId) {
        switch (providerId) {
            case Providers.USERS_PROVIDER:
                return new UsersProvider(this);
            case Providers.RECENT_TRACKS_PROVIDER:
                return null; /* TODO */
        }
        return null;
    }


    /**
     * @param taskExtras task description
     * @return unique task identifier based on:
     * provider_id + method_id + extra parameters
     */
    private String getTaskId(Bundle taskExtras) {
        Set<String> keySet = taskExtras.keySet();
        String keys[] = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keys);

        StringBuilder idBuilder = new StringBuilder();

        for (String k : keys) {
            idBuilder.append('[').append(k).append(':').append(taskExtras.get(k)).append(']');
        }
        return idBuilder.toString();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (mTasks) {
            lastStartId = startId;
            Bundle extras = intent.getExtras();
            String taskId = getTaskId(extras);
            Log.d(TAG, taskId);

            if (!mTasks.containsKey(taskId)) {
                ServiceTask serviceTask = new ServiceTask(extras, taskId);
                mTasks.put(taskId, serviceTask);
                serviceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
            }
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ServiceTask extends AsyncTask<Void, Void, Void> {

        Bundle extras;
        String taskId;

        private ServiceTask(Bundle extras, String taskId) {
            this.extras = extras;
            this.taskId = taskId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            synchronized (mTasks) {

                mTasks.remove(taskId);

                Log.e(TAG, " finished " + taskId);

                if (mTasks.isEmpty()) {
                    stopSelf(lastStartId);
                    Log.e(TAG, "stopSelf with : " + lastStartId);
                }
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            int providerId = extras.getInt(PROVIDER);
            int methodId = extras.getInt(METHOD);

            if ((providerId != 0 && methodId != 0)) {
                IProvider ep = getProvider(providerId);
                if (ep != null) {
                    ep.execMethod(methodId, extras);
                }
            }
            return null;
        }
    }
}