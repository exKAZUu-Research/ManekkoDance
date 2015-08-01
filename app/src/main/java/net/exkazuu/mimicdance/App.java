package net.exkazuu.mimicdance;

import com.deploygate.sdk.DeployGate;

import im.delight.android.ddp.MeteorSingleton;

public class App extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DeployGate.install(this);
        MeteorSingleton.createInstance(this, "ws://mimic-dance-server.herokuapp.com/websocket");
    }
}
