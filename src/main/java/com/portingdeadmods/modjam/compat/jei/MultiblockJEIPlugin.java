package com.portingdeadmods.modjam.compat.jei;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class MultiblockJEIPlugin implements IModPlugin {
    
    private static final List<Multiblock> MULTIBLOCKS = new ArrayList<>();
    
    public static void registerMultiblock(Multiblock multiblock) {
        MULTIBLOCKS.add(multiblock);
    }
    
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Modjam.MODID, "multiblock");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MultiblockJEICategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MultiblockJEICategory.RECIPE_TYPE, MULTIBLOCKS);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (Multiblock multiblock : MULTIBLOCKS) {
            registration.addRecipeCatalyst(
                new ItemStack(multiblock.getUnformedController()),
                MultiblockJEICategory.RECIPE_TYPE
            );
        }
    }
}
