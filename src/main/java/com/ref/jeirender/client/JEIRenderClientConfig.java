package com.ref.jeirender.client;

import com.ref.jeirender.JEIRender;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = JEIRender.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JEIRenderClientConfig {
  private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

  private static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACK_LIST =
      BUILDER
          .comment("A black list of JEI Batch Render.")
          .defineListAllowEmpty(
              "blacklist", List.of("ae2:wrapped_generic_stack"), (obj) -> obj instanceof String);

  private static final ForgeConfigSpec.BooleanValue RENDER_LOG =
      BUILDER.comment("Render Log").define("render_log", false);

  public static final ForgeConfigSpec SPEC = BUILDER.build();

  public static Set<String> blacklist;
  public static boolean render_log;

  @SubscribeEvent
  static void onLoad(final ModConfigEvent event) {
    if (event.getConfig().getSpec() != SPEC) return;
    blacklist = new HashSet<>(BLACK_LIST.get());
    render_log = RENDER_LOG.get();
  }
}
