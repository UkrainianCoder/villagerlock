package com.villagerlock.neoforge;

import com.villagerlock.VillagerLock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod("villagerlock")
public class VillagerLockNeoForge {
	public VillagerLockNeoForge(IEventBus modEventBus) {
		NeoForgePlatformHelper.init(modEventBus);
		VillagerLock.init();
	}
}
