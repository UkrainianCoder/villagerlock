package com.villagerlock;

import net.minecraft.SharedConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VillagerLock {
	public static final String MOD_ID = "villager-lock";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean IS_MC_263 = isVersionAtLeast(SharedConstants.getCurrentVersion().name(), 26, 3);

	public static void init() {
		if (IS_MC_263) {
			LOGGER.info("Features for 26.3+ will be enabled");
		}

		ModBlocks.initialize();
	}

	private static boolean isVersionAtLeast(String current, int requiredMajor, int requiredMinor) {
		String[] parts = current.split("\\.");

		int major = Integer.parseInt(parts[0]);
		int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;

		return major > requiredMajor || major == requiredMajor && minor >= requiredMinor;
	}
}
