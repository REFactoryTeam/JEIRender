package com.ref.jeirender.client;

import com.ref.jeirender.JEIRender;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = JEIRender.MOD_ID)
public class JEIRenderClientConfig {
  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

  private static final ModConfigSpec.ConfigValue<List<? extends String>> BLACK_LIST =
      BUILDER
          .comment("A black list of JEI Batch Render.")
          .defineListAllowEmpty(
              "blacklist", List.of("WrappedGenericStack[wrapped_generic_stack]"), ()->"",(obj) -> obj instanceof String);

  private static final ModConfigSpec.BooleanValue RENDER_LOG =
      BUILDER.comment("Render Log").define("render_log", false);

  public static final ModConfigSpec SPEC = BUILDER.build();

  public static Set<String> blacklist;
  public static boolean render_log;

  @SubscribeEvent
  static void onLoad(final ModConfigEvent event) {
    if (event.getConfig().getSpec() != SPEC) return;
    blacklist = new HashSet<>(BLACK_LIST.get());
    render_log = RENDER_LOG.get();
    JEIRender.THROTTLED_LOGGER.clear();
  }
}
