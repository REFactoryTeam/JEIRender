package com.ref.jeirender;

import com.mojang.logging.LogUtils;
import com.ref.jeirender.client.JEIRenderClientConfig;
import com.ref.jeirender.client.ThrottledLogger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(JEIRender.MOD_ID)
public class JEIRender {

  public static final String MOD_ID = "jeirender";

  public static final Logger LOGGER = LogUtils.getLogger();

  public static final ThrottledLogger THROTTLED_LOGGER = new ThrottledLogger("JEIRender-Debug");

  public JEIRender(IEventBus modEventBus, ModContainer modContainer) {
    modContainer.registerConfig(ModConfig.Type.CLIENT, JEIRenderClientConfig.SPEC);
  }
}
