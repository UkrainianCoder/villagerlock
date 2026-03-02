package com.villagerlock.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.villagerlock.tasks.AssignProfessionOnVillagePostTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(VillagerTaskListProvider.class)
public class VillagerMixin {
	@Inject(method = "createIdleTasks", at = @At("RETURN"), cancellable = true)
	private static void injectMyTask(RegistryEntry<VillagerProfession> registryEntry, float speed, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>> cir) {
		List<Pair<Integer, ? extends Task<? super VillagerEntity>>> tasks = new ArrayList<>(cir.getReturnValue());
		tasks.add(Pair.of(2, new AssignProfessionOnVillagePostTask()));
		cir.setReturnValue(ImmutableList.copyOf(tasks));
	}
}
