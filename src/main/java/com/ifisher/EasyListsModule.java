package com.ifisher;

import com.google.inject.Scopes;
import com.ifisher.models.ChecklistStore;
import roboguice.config.AbstractAndroidModule;

public class EasyListsModule extends AbstractAndroidModule {
    @Override
    protected void configure() {
        bind(ChecklistStore.class).in(Scopes.SINGLETON);
    }
}
