package com.ifisher;

import com.google.inject.Module;
import roboguice.application.RoboApplication;

import java.util.List;

public class EasyListsApplication extends RoboApplication {
    private Module module = new EasyListsModule();

    @Override protected void addApplicationModules(List<Module> modules) {
        modules.add(module);
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
