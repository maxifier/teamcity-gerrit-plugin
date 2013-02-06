package com.maxifier.teamcity.gerrit;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aleksey.didik@maxifier.com (Aleksey Didik)
 */
public class GerritRunType extends RunType {

    private final PluginDescriptor pluginDescriptor;

    public GerritRunType(RunTypeRegistry runTypeRegistry,
                         PluginDescriptor pluginDescriptor) {
        this.pluginDescriptor = pluginDescriptor;
        runTypeRegistry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return "GerritVerification";
    }

    @Override
    public String getDisplayName() {
        return "Gerrit Verification";
    }

    @Override
    public String getDescription() {
        return "Label Verify changes in Gerrit";
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new PropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> properties) {
                ArrayList<InvalidProperty> invalidProperties = new ArrayList<InvalidProperty>(2);
                if ("".equals(properties.get("gerritHost"))) {
                    invalidProperties.add(new InvalidProperty("gerritHost", "Should not be empty."));
                }
                try {
                    Integer.decode(properties.get("gerritPort"));
                } catch (Exception e) {
                    invalidProperties.add(new InvalidProperty("gerritPort", "Should be number"));
                }
                return invalidProperties;
            }
        };
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return pluginDescriptor.getPluginResourcesPath("editGerritRunnerParameters.jsp");
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return pluginDescriptor.getPluginResourcesPath("viewGerritRunnerParameters.jsp");
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        HashMap<String, String> defaultProperties = new HashMap<String, String>();
        defaultProperties.put("gerritPort", "22");
        return defaultProperties;
    }
}
