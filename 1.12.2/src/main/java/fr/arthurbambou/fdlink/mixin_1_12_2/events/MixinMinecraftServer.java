package fr.arthurbambou.fdlink.mixin_1_12_2.events;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_12_2.MinecraftServer1_12_2;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin({MinecraftServer.class})
public class MixinMinecraftServer {

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;setupServer()Z"
            )},
            method = {"run"}
    )
    private void beforeSetupServer(CallbackInfo info) {
        FDLink.getDiscordBot().serverStarting();
    }

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;setFavicon(Lnet/minecraft/server/ServerMetadata;)V",
                    ordinal = 0
            )},
            method = {"run"}
    )
    private void afterSetupServer(CallbackInfo info) {
        FDLink.getDiscordBot().serverStarted();
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"shutdown"}
    )
    private void beforeShutdownServer(CallbackInfo info) {
        FDLink.getDiscordBot().serverStopping();
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"shutdown"}
    )
    private void afterShutdownServer(CallbackInfo info) {
        FDLink.getDiscordBot().serverStopped();
    }

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;method_33183()V"
            )},
            method = {"method_33182"}
    )
    private void onStartTick(CallbackInfo ci) {
        FDLink.getDiscordBot().serverTick(new MinecraftServer1_12_2((MinecraftServer)(Object) this));
    }
}
