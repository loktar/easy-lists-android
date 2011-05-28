package com.pivotallabs.injected;

import android.util.Log;
import com.google.inject.Scopes;
import com.ifisher.models.ChecklistStore;
import roboguice.config.AbstractAndroidModule;
import roboguice.util.Ln;

import java.util.Date;

public class RobolectricSampleTestModule extends AbstractAndroidModule {

    @Override protected void configure() {
        bind(Counter.class).in(Scopes.SINGLETON);
        bind(Date.class).toProvider(FakeDateProvider.class);
        bind(Ln.BaseConfig.class).toInstance(new RobolectricLoggerConfig());
        // Easy Lists config
        bind(ChecklistStore.class).in(Scopes.SINGLETON);
    }


    static class RobolectricLoggerConfig extends Ln.BaseConfig {
        public RobolectricLoggerConfig() {
            super();
            this.packageName = "robo";
            this.minimumLogLevel = Log.VERBOSE;
            this.scope = "ROBO";
        }
    }
}