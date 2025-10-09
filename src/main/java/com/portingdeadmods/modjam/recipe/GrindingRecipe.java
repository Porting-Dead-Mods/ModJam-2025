package com.portingdeadmods.modjam.recipe;

import com.portingdeadmods.modjam.registries.MJItems;
import com.portingdeadmods.modjam.registries.MJRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GrindingRecipe extends CustomRecipe {
    public GrindingRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasPestle = false;
        boolean hasMortar = false;
        boolean hasInput = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            Item item = stack.getItem();
            
            if (item == MJItems.PESTLE.get()) {
                if (hasPestle) return false;
                hasPestle = true;
            } else if (item == MJItems.MORTAR.get()) {
                if (hasMortar) return false;
                hasMortar = true;
            } else if (!stack.isEmpty()) {
                if (hasInput) return false;
                hasInput = true;
            }
        }

        return hasPestle && hasMortar && hasInput;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.Provider registryAccess) {
        ItemStack inputItem = ItemStack.EMPTY;
        
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && 
                stack.getItem() != MJItems.PESTLE.get() && 
                stack.getItem() != MJItems.MORTAR.get()) {
                inputItem = stack;
                break;
            }
        }

        if (inputItem.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() == MJItems.PESTLE.get() || stack.getItem() == MJItems.MORTAR.get()) {
                ItemStack copy = stack.copy();
                copy.hurtAndBreak(1, null, null);
                if (!copy.isEmpty()) {
                    remainingItems.set(i, copy);
                }
            }
        }
        
        return remainingItems;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return MJRecipes.GRINDING.get();
    }
}
