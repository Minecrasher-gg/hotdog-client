package net.minecrashergg;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class CommandHandler {
    public static void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("listmusic")
                    .executes(ctx -> {
                        MusicManager.playMusicList();
                        return 1;
                    }));

            dispatcher.register(ClientCommandManager.literal("play")
                    .then(ClientCommandManager.argument("songNumber", IntegerArgumentType.integer(1))
                            .executes(ctx -> {
                                int num = IntegerArgumentType.getInteger(ctx, "songNumber");
                                MusicManager.playSong(num);
                                return 1;
                            })));

            dispatcher.register(ClientCommandManager.literal("stop")
                    .executes(ctx -> {
                        MusicManager.stopSong();
                        return 1;
                    }));
        });
    }
}
