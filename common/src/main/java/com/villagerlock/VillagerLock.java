package com.villagerlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VillagerLock {
	public static final String MOD_ID = "villager-lock";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void init() {
		ModBlocks.initialize();
	}
}
