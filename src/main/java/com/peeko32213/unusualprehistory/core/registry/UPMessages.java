package com.peeko32213.unusualprehistory.core.registry;

import com.peeko32213.unusualprehistory.UnusualPrehistory;
import com.peeko32213.unusualprehistory.common.networking.packet.AmberProtectionSyncS2CPacket;
import com.peeko32213.unusualprehistory.common.networking.packet.SyncItemStackC2SPacket;
import com.peeko32213.unusualprehistory.core.registry.util.KeyInputMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class UPMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(UnusualPrehistory.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SyncItemStackC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncItemStackC2SPacket::new)
                .encoder(SyncItemStackC2SPacket::toBytes)
                .consumerMainThread(SyncItemStackC2SPacket::handle)
                .add();

        net.messageBuilder(AmberProtectionSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AmberProtectionSyncS2CPacket::new)
                .encoder(AmberProtectionSyncS2CPacket::toBytes)
                .consumerMainThread(AmberProtectionSyncS2CPacket::handle)
                .add();

        net.messageBuilder(KeyInputMessage.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(KeyInputMessage::new)
                .encoder(KeyInputMessage::toBytes)
                .consumerMainThread(KeyInputMessage::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
    }
