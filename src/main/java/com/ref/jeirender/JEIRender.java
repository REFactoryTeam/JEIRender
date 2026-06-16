package com.ref.jeirender;

import com.mojang.logging.LogUtils;
import com.ref.jeirender.client.JEIRenderClientConfig;
import com.ref.jeirender.client.ThrottledLogger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(JEIRender.MOD_ID)
public class JEIRender {

  public static final String MOD_ID = "jeirender";

  public static final Logger LOGGER = LogUtils.getLogger();

  public static final ThrottledLogger THROTTLED_LOGGER = new ThrottledLogger("JEIRender-Debug");

  public JEIRender(FMLJavaModLoadingContext context) {
    context.registerConfig(ModConfig.Type.CLIENT, JEIRenderClientConfig.SPEC);
  }
}
