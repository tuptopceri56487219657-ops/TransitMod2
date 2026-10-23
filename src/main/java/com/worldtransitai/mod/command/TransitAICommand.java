package com.worldtransitai.mod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.worldtransitai.mod.ai.MetroAIGenerator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class TransitAICommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("transitai")
            .then(Commands.literal("build")
                .then(Commands.argument("transportType", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        builder.suggest("metro");
                        builder.suggest("railway");
                        builder.suggest("funicular");
                        builder.suggest("air_port");
                        builder.suggest("sea_port");
                        return builder.buildFuture();
                    })
                    .then(Commands.argument("city", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            String type = StringArgumentType.getString(ctx, "transportType");
                            String city = StringArgumentType.getString(ctx, "city");
                            MetroAIGenerator.generateCityTransitAsync(ctx.getSource().getLevel(), player, city, type);
                            return 1;
                        })
                    )
                )
            )
        );
    }
}
