package com.github.vitorm3lo.jbossplugin.services;

import com.github.vitorm3lo.jbossplugin.model.InstanceState;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(name = "jboss-servers", storages = {@Storage("JBossServers.xml")})
public class PersistenceService implements PersistentStateComponent<PersistenceService.State> {

    private State state = new State();

    public static class State {
        public State() {}
        public List<InstanceState> instanceState = new ArrayList<>();
    }

    public static PersistenceService getInstance() {
        return ServiceManager.getService(PersistenceService.class);
    }

    @Override
    public @Nullable PersistenceService.State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull PersistenceService.State state) {
        this.state = state;
    }
}
