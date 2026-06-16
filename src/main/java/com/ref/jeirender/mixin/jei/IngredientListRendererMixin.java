package com.ref.jeirender.mixin.jei;

import com.ref.jeirender.JEIRender;
import com.ref.jeirender.client.IngredientRender;
import com.ref.jeirender.client.JEIRenderClientConfig;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.Internal;
import mezz.jei.gui.overlay.IngredientListRenderer;
import mezz.jei.gui.overlay.IngredientListSlot;
import mezz.jei.gui.overlay.elements.IElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IngredientListRenderer.class, remap = false)
@OnlyIn(Dist.CLIENT)
public abstract class IngredientListRendererMixin {

  @Unique private final List<IngredientRender<?>> JEIRender$specialEntries = new ArrayList<>();
  @Unique private IIngredientManager JEIRender$cachedManager;

  @Unique
  private void JEIRender$updateCache() {
    if (JEIRender$cachedManager == null) {
      IJeiRuntime runtime = Internal.getJeiRuntime();
      JEIRender$cachedManager = runtime.getIngredientManager();
    }
  }

  @Unique
  private <T> boolean JEIRender$isBlacklisted(ITypedIngredient<T> typedIngredient) {
    if (typedIngredient == null || JEIRender$cachedManager == null) {
      return false;
    }

    IIngredientHelper<T> helper =
        JEIRender$cachedManager.getIngredientHelper(typedIngredient.getType());
    String uid =
        helper.getUniqueId(
            typedIngredient.getIngredient(),
            mezz.jei.api.ingredients.subtypes.UidContext.Ingredient);

    if (JEIRenderClientConfig.render_log) {
      JEIRender.THROTTLED_LOGGER.log(uid);
    }

    if (JEIRenderClientConfig.blacklist.isEmpty()) {
      return false;
    }

    return JEIRenderClientConfig.blacklist.contains(uid);
  }

  @Inject(method = "set(ILjava/util/List;)V", at = @At("HEAD"))
  private void onSetHead(int skip, List<?> arg1, CallbackInfo ci) {
    JEIRender$updateCache();
    JEIRender$specialEntries.clear();
    JEIRender.THROTTLED_LOGGER.clear();
  }

  @Inject(method = "set(ILjava/util/List;)V", at = @At("TAIL"))
  private void onSetTail(int skip, List<?> arg1, CallbackInfo ci) {
    JEIRender.THROTTLED_LOGGER.flush();
  }

  @Inject(method = "clear()V", at = @At("HEAD"))
  private void onClear(CallbackInfo ci) {
    JEIRender$specialEntries.clear();
    JEIRender.THROTTLED_LOGGER.flush();
  }

  @Inject(
      method = "addRenderElement(Lmezz/jei/gui/overlay/IngredientListSlot;)V",
      at = @At("HEAD"),
      cancellable = true)
  private void interceptAddRenderElement(IngredientListSlot ingredientListSlot, CallbackInfo ci) {
    if (ingredientListSlot.getArea().getX() > 200) return;
    ingredientListSlot
        .getOptionalElement()
        .map(IElement::getTypedIngredient)
        .ifPresent(
            typedIngredient -> {
              if (JEIRender$isBlacklisted(typedIngredient)) {
                JEIRender$specialEntries.add(
                    new IngredientRender<>(
                        typedIngredient,
                        ingredientListSlot.getArea().getX() + 1,
                        ingredientListSlot.getArea().getY() + 1));
                ci.cancel();
              }
            });
  }

  @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("TAIL"))
  private void onRenderTail(GuiGraphics guiGraphics, CallbackInfo ci) {
    if (JEIRender$specialEntries.isEmpty() || JEIRender$cachedManager == null) return;
    for (IngredientRender<?> jeiRender$specialEntry : JEIRender$specialEntries) {
      JEIRender$renderGeneric(guiGraphics, JEIRender$cachedManager, jeiRender$specialEntry);
    }
  }

  @Unique
  private <T> void JEIRender$renderGeneric(
      GuiGraphics guiGraphics, IIngredientManager manager, IngredientRender<T> entry) {
    ITypedIngredient<T> typed = entry.ingredient();
    IIngredientRenderer<T> renderer = manager.getIngredientRenderer(typed.getType());
    renderer.render(guiGraphics, typed.getIngredient(), entry.x(), entry.y());
  }
}
