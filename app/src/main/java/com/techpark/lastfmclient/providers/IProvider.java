package com.techpark.lastfmclient.providers;

import android.os.Bundle;

/**
 * Created by Andrew Gov on 08.11.14.
 */
public interface IProvider {
    void execMethod(int methodId, Bundle extraData);
}
