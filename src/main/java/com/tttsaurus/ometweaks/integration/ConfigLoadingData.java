package com.tttsaurus.ometweaks.integration;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.Method;

public class ConfigLoadingData
{
    public final Method loadConfigMethod;
    public final ImmutableList<String> stages;

    public ConfigLoadingData(Method loadConfigMethod, String[] stages)
    {
        this.loadConfigMethod = loadConfigMethod;
        this.stages = ImmutableList.copyOf(stages);
    }
}
