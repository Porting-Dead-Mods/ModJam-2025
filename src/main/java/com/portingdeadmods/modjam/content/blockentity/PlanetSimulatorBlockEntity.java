package com.portingdeadmods.modjam.content.blockentity;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.capabilities.ReadOnlyEnergyStorage;
import com.portingdeadmods.modjam.capabilities.ReadOnlyFluidHandler;
import com.portingdeadmods.modjam.capabilities.ReadOnlyItemHandler;
import com.portingdeadmods.modjam.capabilities.UpgradeItemHandler;
import com.portingdeadmods.modjam.content.block.UpgradeBlockEntity;
import com.portingdeadmods.modjam.networking.SyncPlanetSimulatorDataPayload;
import com.portingdeadmods.modjam.utils.NumberFormatUtils;
import com.portingdeadmods.modjam.content.items.UpgradeItem;
import com.portingdeadmods.modjam.content.blockentity.bus.AbstractBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.EnergyInputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.FluidInputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.FluidOutputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.ItemInputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.ItemOutputBusBlockEntity;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.modjam.content.recipe.PlanetPowerRecipe;
import com.portingdeadmods.modjam.content.recipe.PlanetSimulatorRecipe;
import com.portingdeadmods.modjam.data.BusType;
import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.modjam.data.UpgradeType;
import com.portingdeadmods.modjam.registries.*;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PlanetSimulatorBlockEntity extends ContainerBlockEntity implements MultiblockEntity, MenuProvider, UpgradeBlockEntity {
    public static final Set<ResourceKey<UpgradeType>> SUPPORTED_UPGRADES = Set.of(
            ResourceKey.create(MJRegistries.UPGRADE_TYPE_KEY, Modjam.rl("speed")),
            ResourceKey.create(MJRegistries.UPGRADE_TYPE_KEY, Modjam.rl("energy")),
            ResourceKey.create(MJRegistries.UPGRADE_TYPE_KEY, Modjam.rl("luck"))
    );
    private MultiblockData multiblockData;
    private final UpgradeItemHandler upgradeItemHandler;

    private int progress = 0;
    private int maxProgress = 0;
    private int energyPerTick = 0;
    private boolean isProcessing = false;
    private PlanetSimulatorRecipe currentRegularRecipe = null;
    private PlanetPowerRecipe currentPowerRecipe = null;
    
    private String clientDisplayText = "";

    public String getClientDisplayText() {
        return clientDisplayText;
    }
    
    public void setClientDisplayText(String text) {
        this.clientDisplayText = text;
    }
    
    public int getProgress() {
        return progress;
    }
    
    public int getMaxProgress() {
        return maxProgress;
    }
    
    public int getEnergyPerTick() {
        return energyPerTick;
    }
    
    public boolean isProcessing() {
        return isProcessing;
    }

    public List<AbstractBusBlockEntity> getInputBusses() {
        return getBusses(true);
    }

    public List<AbstractBusBlockEntity> getOutputBusses() {
        return getBusses(false);
    }

    public int getTotalInputEnergy() {
        return getTotalInputEnergy(getInputBusses());
    }

    public int getTotalMaxInputEnergy() {
        int total = 0;
        for (AbstractBusBlockEntity bus : getInputBusses()) {
            if (bus.getBusType() == BusType.ENERGY && bus instanceof EnergyInputBusBlockEntity energyBus) {
                total += energyBus.getEnergyStorage().getMaxEnergyStored();
            }
        }
        return total;
    }

    public Map<Item, Integer> getAllInputItems() {
        return gatherAllInputItems(getInputBusses());
    }

    public FluidStack getAllInputFluids() {
        return gatherAllInputFluids(getInputBusses());
    }

    public Map<Item, Integer> getAllOutputItems() {
        Map<Item, Integer> itemMap = new HashMap<>();
        for (AbstractBusBlockEntity bus : getOutputBusses()) {
            if (bus.getBusType() == BusType.ITEM && bus instanceof ItemOutputBusBlockEntity itemBus) {
                net.neoforged.neoforge.items.ItemStackHandler handler = itemBus.getItemHandler();
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        itemMap.merge(stack.getItem(), stack.getCount(), Integer::sum);
                    }
                }
            }
        }
        return itemMap;
    }

    public FluidStack getAllOutputFluids() {
        for (AbstractBusBlockEntity bus : getOutputBusses()) {
            if (bus.getBusType() == BusType.FLUID && bus instanceof FluidOutputBusBlockEntity fluidBus) {
                FluidStack fluid = fluidBus.getFluidTank().getFluid();
                if (!fluid.isEmpty()) {
                    return fluid;
                }
            }
        }
        return FluidStack.EMPTY;
    }

    public PlanetSimulatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MJBlockEntities.PLANET_SIMULATOR.get(), blockPos, blockState);
        this.addItemHandler(1, (slot, item) -> item.has(MJDataComponents.PLANET));

        this.upgradeItemHandler = new UpgradeItemHandler(SUPPORTED_UPGRADES) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);

                update();
            }

            @Override
            public void onUpgradeAdded(ResourceKey<UpgradeType> upgrade) {
                super.onUpgradeAdded(upgrade);

                PlanetSimulatorBlockEntity.this.onUpgradeAdded(upgrade);
            }

            @Override
            public void onUpgradeRemoved(ResourceKey<UpgradeType> upgrade) {
                super.onUpgradeRemoved(upgrade);

                PlanetSimulatorBlockEntity.this.onUpgradeRemoved(upgrade);
            }
        };

    }

    @Override
    public void commonTick() {
        super.commonTick();

        if (this.level.getGameTime() % 20 == 0 && !this.getBlockState().getValue(Multiblock.FORMED)) {
            MJMultiblocks.PLANET_SIMULATOR.get().form(this.level, this.worldPosition);
        }

        if (this.getBlockState().getValue(Multiblock.FORMED)) {
            processRecipes();
        }
        
        if (!this.level.isClientSide && this.level.getGameTime() % 5 == 0) {
            syncToNearbyPlayers();
        }
    }
    
    private void syncToNearbyPlayers() {
        if (this.level == null || this.level.isClientSide) return;
        
        String displayText = buildDisplayText();
        
        SyncPlanetSimulatorDataPayload payload = 
            new SyncPlanetSimulatorDataPayload(
                this.worldPosition,
                displayText
            );
        
        this.level.players().stream()
            .filter(player -> player instanceof ServerPlayer)
            .map(player -> (ServerPlayer) player)
            .filter(player -> {
                if (player.containerMenu instanceof PlanetSimulatorMenu menu) {
                    return menu.blockEntity.getBlockPos().equals(this.worldPosition);
                }
                return false;
            })
            .forEach(player -> PacketDistributor.sendToPlayer(player, payload));
    }
    
    private String buildDisplayText() {
        StringBuilder info = new StringBuilder();
        
        int energyStored = getTotalInputEnergy(getInputBusses());
        int maxEnergy = getTotalMaxInputEnergy();
        info.append("Energy: ").append(NumberFormatUtils.formatEnergy(energyStored))
            .append(" / ").append(NumberFormatUtils.formatEnergy(maxEnergy)).append(" FE\n");
        
        if (!isProcessing) {
            return info.toString();
        }
        
        if (currentRegularRecipe != null) {
            info.append("\nRecipe: Planet Simulation");
        } else if (currentPowerRecipe != null) {
            info.append("\nRecipe: Power Generation");
        } else {
            info.append("\nRecipe: Unknown");
        }
        
        info.append("\nEnergy/t: ");
        if (currentPowerRecipe != null) {
            info.append("+");
        }
        info.append(NumberFormatUtils.formatEnergy(energyPerTick)).append(" FE/t");
        
        int timeLeft = (maxProgress - progress) / 20;
        info.append("\nTime Left: ").append(timeLeft).append("s");
        info.append("\nProgress: ").append(progress).append("/").append(maxProgress);
        
        if (currentRegularRecipe != null) {
            info.append("\n\nOutputs:");
            for (PlanetSimulatorRecipe.WeightedOutput output : currentRegularRecipe.outputs()) {
                float chance = applyLuckUpgrade(output.chance());
                info.append("\n  ");
                if (output.itemStack().isPresent()) {
                    info.append(output.itemStack().get().getHoverName().getString());
                } else if (output.fluidStack().isPresent()) {
                    info.append(output.fluidStack().get().getHoverName().getString());
                }
                info.append(" (").append(String.format("%.1f%%", chance * 100)).append(")");
            }
        }
        
        return info.toString();
    }

    private void processRecipes() {

        if (this.level == null || this.level.isClientSide) return;
        this.update();

        ItemStack planetCard = this.getItemHandler().getStackInSlot(0);
        if (planetCard.isEmpty() || !planetCard.has(MJDataComponents.PLANET)) {
            resetProgress();
            return;
        }

        PlanetComponent planetComponent = planetCard.get(MJDataComponents.PLANET);
        if (planetComponent == null) {
            resetProgress();
            return;
        }

        List<AbstractBusBlockEntity> inputBusses = getBusses(true);
        List<AbstractBusBlockEntity> outputBusses = getBusses(false);

        PlanetSimulatorRecipe regularRecipe = findMatchingRegularRecipe(planetComponent, inputBusses);
        PlanetPowerRecipe powerRecipe = findMatchingPowerRecipe(planetComponent, inputBusses);

        if (regularRecipe != null) {
            processRegularRecipe(regularRecipe, inputBusses, outputBusses);
        } else if (powerRecipe != null) {
            processPowerRecipe(powerRecipe, inputBusses);
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 0;
        energyPerTick = 0;
        isProcessing = false;
        currentRegularRecipe = null;
        currentPowerRecipe = null;
    }

    private List<AbstractBusBlockEntity> getBusses(boolean input) {
        List<AbstractBusBlockEntity> busses = new ArrayList<>();
        if (this.multiblockData == null || this.level == null) return busses;

        MultiblockLayer[] layers = this.multiblockData.layers();
        if (layers == null) return busses;

        for (MultiblockLayer layer : layers) {
            for (int i = 0; i < layer.layer().length; i++) {
                BlockPos pos = getBlockPosFromLayer(layer, i);
                if (pos != null) {
                    BlockEntity be = this.level.getBlockEntity(pos);
                    if (be instanceof AbstractBusBlockEntity bus && bus.isInput() == input) {
                        busses.add(bus);
                    }
                }
            }
        }
        return busses;
    }

    private BlockPos getBlockPosFromLayer(MultiblockLayer layer, int index) {
        if (this.multiblockData == null) return null;
        
        HorizontalDirection direction = this.multiblockData.direction();
        if (direction == null) return null;

        int width = layer.widths().leftInt();
        int x = index % width;
        int z = index / width;
        int y = 0;

        for (int i = 0; i < this.multiblockData.layers().length; i++) {
            if (this.multiblockData.layers()[i] == layer) {
                y = i;
                break;
            }
        }

        Vec3i relativeControllerPos = MultiblockHelper.getRelativeControllerPos(MJMultiblocks.PLANET_SIMULATOR.get());
        BlockPos firstBlockPos = MultiblockHelper.getFirstBlockPos(direction, this.worldPosition, relativeControllerPos);
        
        return MultiblockHelper.getCurPos(firstBlockPos, new Vec3i(x, y, z), direction);
    }

    private PlanetSimulatorRecipe findMatchingRegularRecipe(PlanetComponent planetComponent, List<AbstractBusBlockEntity> inputBusses) {
        if (this.level == null || planetComponent.planetType().isEmpty()) return null;

        var planetTypeRegistry = this.level.registryAccess().lookupOrThrow(MJRegistries.PLANET_TYPE_KEY);
        var planetTypeKey = planetTypeRegistry.listElements()
                .filter(holder -> holder.value().equals(planetComponent.planetType().get()))
                .map(Holder.Reference::key)
                .findFirst()
                .orElse(null);

        if (planetTypeKey == null) return null;

        return this.level.getRecipeManager()
                .getAllRecipesFor(PlanetSimulatorRecipe.TYPE)
                .stream()
                .filter(recipe -> recipe.value().planetType().equals(planetTypeKey))
                .filter(recipe -> hasRequiredInputs(recipe.value(), inputBusses))
                .sorted((r1, r2) -> Integer.compare(getRecipeSpecificity(r2.value(), inputBusses), getRecipeSpecificity(r1.value(), inputBusses)))
                .map(RecipeHolder::value)
                .findFirst()
                .orElse(null);
    }

    private PlanetPowerRecipe findMatchingPowerRecipe(PlanetComponent planetComponent, List<AbstractBusBlockEntity> inputBusses) {
        if (this.level == null || planetComponent.planetType().isEmpty()) return null;

        var planetTypeRegistry = this.level.registryAccess().lookupOrThrow(MJRegistries.PLANET_TYPE_KEY);
        var planetTypeKey = planetTypeRegistry.listElements()
                .filter(holder -> holder.value().equals(planetComponent.planetType().get()))
                .map(Holder.Reference::key)
                .findFirst()
                .orElse(null);

        if (planetTypeKey == null) return null;

        return this.level.getRecipeManager()
                .getAllRecipesFor(PlanetPowerRecipe.TYPE)
                .stream()
                .filter(recipe -> recipe.value().planetType().equals(planetTypeKey))
                .filter(recipe -> hasRequiredInputs(recipe.value(), inputBusses))
                .sorted((r1, r2) -> Integer.compare(getRecipeSpecificity(r2.value(), inputBusses), getRecipeSpecificity(r1.value(), inputBusses)))
                .map(RecipeHolder::value)
                .findFirst()
                .orElse(null);
    }

    private int getRecipeSpecificity(PlanetSimulatorRecipe recipe, List<AbstractBusBlockEntity> inputBusses) {
        int specificity = 0;
        
        // Catalysts are required but not consumed - give them medium weight
        specificity += recipe.catalysts().size() * 100;
        for (IngredientWithCount catalyst : recipe.catalysts()) {
            specificity += catalyst.count();
        }
        
        // Inputs are consumed - give them higher weight since they're more restrictive
        specificity += recipe.inputs().size() * 200;
        for (IngredientWithCount input : recipe.inputs()) {
            specificity += input.count() * 2;
        }
        
        if (recipe.fluidInput().isPresent()) {
            specificity += 1000;
            for (FluidStack fluid : recipe.fluidInput().get().getStacks()) {
                specificity += fluid.getAmount();
            }
        }
        
        return specificity;
    }

    private int getRecipeSpecificity(PlanetPowerRecipe recipe, List<AbstractBusBlockEntity> inputBusses) {
        int specificity = 0;
        
        // Catalysts are required but not consumed - give them medium weight
        specificity += recipe.catalysts().size() * 100;
        for (IngredientWithCount catalyst : recipe.catalysts()) {
            specificity += catalyst.count();
        }
        
        // Inputs are consumed - give them higher weight since they're more restrictive
        specificity += recipe.inputs().size() * 200;
        for (IngredientWithCount input : recipe.inputs()) {
            specificity += input.count() * 2;
        }
        
        if (recipe.fluidInput().isPresent()) {
            specificity += 1000;
            for (FluidStack fluid : recipe.fluidInput().get().getStacks()) {
                specificity += fluid.getAmount();
            }
        }
        
        return specificity;
    }

    private boolean hasRequiredInputs(PlanetSimulatorRecipe recipe, List<AbstractBusBlockEntity> inputBusses) {
        Map<Item, Integer> availableItems = gatherAllInputItems(inputBusses);
        FluidStack availableFluid = gatherAllInputFluids(inputBusses);

        // Check catalysts (required but not consumed)
        for (IngredientWithCount catalyst : recipe.catalysts()) {
            int totalFound = 0;
            for (Map.Entry<Item, Integer> entry : availableItems.entrySet()) {
                if (catalyst.ingredient().test(new ItemStack(entry.getKey()))) {
                    totalFound += entry.getValue();
                }
            }
            if (totalFound < catalyst.count()) return false;
        }

        // Check inputs (required and consumed)
        for (IngredientWithCount input : recipe.inputs()) {
            int totalFound = 0;
            for (Map.Entry<Item, Integer> entry : availableItems.entrySet()) {
                if (input.ingredient().test(new ItemStack(entry.getKey()))) {
                    totalFound += entry.getValue();
                }
            }
            if (totalFound < input.count()) return false;
        }

        if (recipe.fluidInput().isPresent()) {
            if (availableFluid.isEmpty() || !recipe.fluidInput().get().test(availableFluid)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasRequiredInputs(PlanetPowerRecipe recipe, List<AbstractBusBlockEntity> inputBusses) {
        Map<Item, Integer> availableItems = gatherAllInputItems(inputBusses);
        FluidStack availableFluid = gatherAllInputFluids(inputBusses);

        // Check catalysts (required but not consumed)
        for (IngredientWithCount catalyst : recipe.catalysts()) {
            int totalFound = 0;
            for (Map.Entry<Item, Integer> entry : availableItems.entrySet()) {
                if (catalyst.ingredient().test(new ItemStack(entry.getKey()))) {
                    totalFound += entry.getValue();
                }
            }
            if (totalFound < catalyst.count()) return false;
        }

        // Check inputs (required and consumed)
        for (IngredientWithCount input : recipe.inputs()) {
            int totalFound = 0;
            for (Map.Entry<Item, Integer> entry : availableItems.entrySet()) {
                if (input.ingredient().test(new ItemStack(entry.getKey()))) {
                    totalFound += entry.getValue();
                }
            }
            if (totalFound < input.count()) return false;
        }

        if (recipe.fluidInput().isPresent()) {
            if (availableFluid.isEmpty() || !recipe.fluidInput().get().test(availableFluid)) {
                return false;
            }
        }

        return true;
    }

    private Map<Item, Integer> gatherAllInputItems(List<AbstractBusBlockEntity> inputBusses) {
        Map<Item, Integer> itemMap = new HashMap<>();
        for (AbstractBusBlockEntity bus : inputBusses) {
            if (bus.getBusType() == BusType.ITEM && bus instanceof ItemInputBusBlockEntity itemBus) {
                IItemHandler handler = itemBus.getItemHandler();
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        itemMap.merge(stack.getItem(), stack.getCount(), Integer::sum);
                    }
                }
            }
        }
        return itemMap;
    }

    private FluidStack gatherAllInputFluids(List<AbstractBusBlockEntity> inputBusses) {
        for (AbstractBusBlockEntity bus : inputBusses) {
            if (bus.getBusType() == BusType.FLUID && bus instanceof FluidInputBusBlockEntity fluidBus) {
                FluidStack fluid = fluidBus.getFluidTank().getFluid();
                if (!fluid.isEmpty()) {
                    return fluid;
                }
            }
        }
        return FluidStack.EMPTY;
    }

    private int getTotalInputEnergy(List<AbstractBusBlockEntity> inputBusses) {
        int total = 0;
        for (AbstractBusBlockEntity bus : inputBusses) {
            if (bus.getBusType() == BusType.ENERGY && bus instanceof EnergyInputBusBlockEntity energyBus) {
                total += energyBus.getEnergyStorage().getEnergyStored();
            }
        }
        return total;
    }

    private void processRegularRecipe(PlanetSimulatorRecipe recipe, List<AbstractBusBlockEntity> inputBusses, List<AbstractBusBlockEntity> outputBusses) {
        if (!isProcessing) {
            isProcessing = true;
            currentRegularRecipe = recipe;
            currentPowerRecipe = null;
            applyUpgrades(recipe.duration(), recipe.energyPerTick());
            progress = 0;
        }

        int availableEnergy = getTotalInputEnergy(inputBusses);
        if (availableEnergy < energyPerTick) {
            return;
        }

        consumeEnergy(inputBusses, energyPerTick);
        progress++;
        this.setChanged();

        if (progress >= maxProgress) {
            if (consumeInputs(recipe, inputBusses) && canOutputResults(recipe.outputs(), outputBusses)) {
                outputResults(recipe.outputs(), outputBusses);
                resetProgress();
            } else {
                progress = maxProgress - 1;
            }
        }
    }

    private int applyUpgrades(int baseDuration, int baseEnergyPerTick) {
        float duration = baseDuration;
        float energyPerTick = baseEnergyPerTick;
        
        for (ResourceKey<UpgradeType> upgradeTypeKey : SUPPORTED_UPGRADES) {
            int count = getUpgradeAmount(upgradeTypeKey);
            if (count == 0) continue;
            
            UpgradeType upgradeType = level.registryAccess()
                    .lookupOrThrow(MJRegistries.UPGRADE_TYPE_KEY)
                    .get(upgradeTypeKey)
                    .map(holder -> holder.value())
                    .orElse(new UpgradeType(List.of()));
            
            for (UpgradeType.UpgradeEffect effect : upgradeType.getEffects()) {
                switch (effect.getTarget()) {
                    case DURATION -> duration = effect.apply(duration, count);
                    case ENERGY_USAGE -> energyPerTick = effect.apply(energyPerTick, count);
                }
            }
        }
        
        this.maxProgress = Math.max(1, (int) duration);
        this.energyPerTick = Math.max(1, (int) energyPerTick);
        
        return this.maxProgress;
    }

    private float applyLuckUpgrade(float baseChance) {
        if (baseChance >= 1.0f) return baseChance;
        
        float totalBonus = 0.0f;
        
        for (ResourceKey<UpgradeType> upgradeTypeKey : SUPPORTED_UPGRADES) {
            int count = getUpgradeAmount(upgradeTypeKey);
            if (count == 0) continue;
            
            UpgradeType upgradeType = level.registryAccess()
                    .lookupOrThrow(MJRegistries.UPGRADE_TYPE_KEY)
                    .get(upgradeTypeKey)
                    .map(holder -> holder.value())
                    .orElse(new UpgradeType(List.of()));
            
            for (UpgradeType.UpgradeEffect effect : upgradeType.getEffects()) {
                if (effect.getTarget() == UpgradeType.EffectTarget.LUCK_CHANCE) {
                    totalBonus += effect.getPercentPerUpgrade() * count;
                }
            }
        }
        
        return Math.min(1.0f, baseChance * (1.0f + totalBonus / 100.0f));
    }

    private void processPowerRecipe(PlanetPowerRecipe recipe, List<AbstractBusBlockEntity> inputBusses) {
        if (!isProcessing) {
            isProcessing = true;
            currentRegularRecipe = null;
            currentPowerRecipe = recipe;
            applyUpgrades(recipe.duration(), recipe.energyPerTick());
            progress = 0;
        }

        progress++;

        if (progress >= maxProgress) {
            if (consumeInputs(recipe, inputBusses)) {
                generateEnergy(inputBusses, energyPerTick);
                resetProgress();
            } else {
                progress = maxProgress - 1;
            }
        }
    }

    private boolean consumeInputs(PlanetSimulatorRecipe recipe, List<AbstractBusBlockEntity> inputBusses) {
        // DO NOT consume catalysts - they are required but not consumed
        // Only consume inputs
        
        for (IngredientWithCount input : recipe.inputs()) {
            int remaining = input.count();
            for (AbstractBusBlockEntity bus : inputBusses) {
                if (bus.getBusType() == BusType.ITEM && bus instanceof ItemInputBusBlockEntity itemBus) {
                    IItemHandler handler = itemBus.getItemHandler();
                    for (int i = 0; i < handler.getSlots() && remaining > 0; i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (input.ingredient().test(stack)) {
                            int toExtract = Math.min(remaining, stack.getCount());
                            handler.extractItem(i, toExtract, false);
                            remaining -= toExtract;
                        }
                    }
                }
            }
            if (remaining > 0) return false;
        }

        if (recipe.fluidInput().isPresent()) {
            for (AbstractBusBlockEntity bus : inputBusses) {
                if (bus.getBusType() == BusType.FLUID && bus instanceof FluidInputBusBlockEntity fluidBus) {
                    FluidStack fluid = fluidBus.getFluidTank().getFluid();
                    if (recipe.fluidInput().get().test(fluid)) {
                        int amountToDrain = fluid.getAmount();
                        for (FluidStack ingredientFluid : recipe.fluidInput().get().getStacks()) {
                            if (ingredientFluid.getFluid().isSame(fluid.getFluid())) {
                                amountToDrain = Math.min(ingredientFluid.getAmount(), fluid.getAmount());
                                break;
                            }
                        }
                        fluidBus.getFluidTank().drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE);
                        return true;
                    }
                }
            }
            return false;
        }

        return true;
    }

    private boolean consumeInputs(PlanetPowerRecipe recipe, List<AbstractBusBlockEntity> inputBusses) {
        // DO NOT consume catalysts - they are required but not consumed
        // Only consume inputs
        
        for (IngredientWithCount input : recipe.inputs()) {
            int remaining = input.count();
            for (AbstractBusBlockEntity bus : inputBusses) {
                if (bus.getBusType() == BusType.ITEM && bus instanceof ItemInputBusBlockEntity itemBus) {
                    IItemHandler handler = itemBus.getItemHandler();
                    for (int i = 0; i < handler.getSlots() && remaining > 0; i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (input.ingredient().test(stack)) {
                            int toExtract = Math.min(remaining, stack.getCount());
                            handler.extractItem(i, toExtract, false);
                            remaining -= toExtract;
                        }
                    }
                }
            }
            if (remaining > 0) return false;
        }

        if (recipe.fluidInput().isPresent()) {
            for (AbstractBusBlockEntity bus : inputBusses) {
                if (bus.getBusType() == BusType.FLUID && bus instanceof FluidInputBusBlockEntity fluidBus) {
                    FluidStack fluid = fluidBus.getFluidTank().getFluid();
                    if (recipe.fluidInput().get().test(fluid)) {
                        int amountToDrain = fluid.getAmount();
                        for (FluidStack ingredientFluid : recipe.fluidInput().get().getStacks()) {
                            if (ingredientFluid.getFluid().isSame(fluid.getFluid())) {
                                amountToDrain = Math.min(ingredientFluid.getAmount(), fluid.getAmount());
                                break;
                            }
                        }
                        fluidBus.getFluidTank().drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE);
                        return true;
                    }
                }
            }
            return false;
        }

        return true;
    }

    private void consumeEnergy(List<AbstractBusBlockEntity> inputBusses, int amount) {
        int remaining = amount;
        for (AbstractBusBlockEntity bus : inputBusses) {
            if (bus.getBusType() == BusType.ENERGY && bus instanceof EnergyInputBusBlockEntity energyBus && remaining > 0) {
                int extracted = energyBus.getEnergyStorage().extractEnergy(remaining, false);
                remaining -= extracted;
            }
        }
    }

    private void generateEnergy(List<AbstractBusBlockEntity> inputBusses, int amount) {
        int remaining = amount;
        for (AbstractBusBlockEntity bus : inputBusses) {
            if (bus.getBusType() == BusType.ENERGY && bus instanceof EnergyInputBusBlockEntity energyBus && remaining > 0) {
                int received = energyBus.getEnergyStorage().receiveEnergy(remaining, false);
                remaining -= received;
            }
        }
    }

    private boolean canOutputResults(List<PlanetSimulatorRecipe.WeightedOutput> outputs, List<AbstractBusBlockEntity> outputBusses) {
        for (PlanetSimulatorRecipe.WeightedOutput output : outputs) {
            float chance = applyLuckUpgrade(output.chance());
            if (Math.random() > chance) continue;

            if (output.itemStack().isPresent()) {
                ItemStack stack = output.itemStack().get();
                if (!canInsertItem(stack, outputBusses)) {
                    return false;
                }
            }

            if (output.fluidStack().isPresent()) {
                FluidStack stack = output.fluidStack().get();
                if (!canInsertFluid(stack, outputBusses)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canInsertItem(ItemStack stack, List<AbstractBusBlockEntity> outputBusses) {
        List<ItemOutputBusBlockEntity> itemBusses = outputBusses.stream()
                .filter(bus -> bus.getBusType() == BusType.ITEM)
                .map(bus -> (ItemOutputBusBlockEntity) bus)
                .collect(Collectors.toList());

        for (ItemOutputBusBlockEntity bus : itemBusses) {
            IItemHandler handler = bus.getItemHandler();
            ItemStack remaining = stack.copy();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack existing = handler.getStackInSlot(i);
                if (existing.isEmpty()) {
                    return true;
                } else if (ItemStack.isSameItemSameComponents(existing, stack)) {
                    int maxStack = Math.min(handler.getSlotLimit(i), stack.getMaxStackSize());
                    if (existing.getCount() < maxStack) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canInsertFluid(FluidStack stack, List<AbstractBusBlockEntity> outputBusses) {
        List<FluidOutputBusBlockEntity> fluidBusses = outputBusses.stream()
                .filter(bus -> bus.getBusType() == BusType.FLUID)
                .map(bus -> (FluidOutputBusBlockEntity) bus)
                .collect(Collectors.toList());

        for (FluidOutputBusBlockEntity bus : fluidBusses) {
            FluidStack existing = bus.getFluidTank().getFluid();
            if (existing.isEmpty()) {
                return true;
            } else if (existing.isFluidEqual(stack)) {
                if (existing.getAmount() + stack.getAmount() <= bus.getFluidTank().getCapacity()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void outputResults(List<PlanetSimulatorRecipe.WeightedOutput> outputs, List<AbstractBusBlockEntity> outputBusses) {
        for (PlanetSimulatorRecipe.WeightedOutput output : outputs) {
            float chance = applyLuckUpgrade(output.chance());
            if (Math.random() > chance) continue;

            if (output.itemStack().isPresent()) {
                insertItem(output.itemStack().get(), outputBusses);
            }

            if (output.fluidStack().isPresent()) {
                insertFluid(output.fluidStack().get(), outputBusses);
            }
        }
    }

    private void insertItem(ItemStack stack, List<AbstractBusBlockEntity> outputBusses) {
        List<ItemOutputBusBlockEntity> itemBusses = outputBusses.stream()
                .filter(bus -> bus.getBusType() == BusType.ITEM)
                .map(bus -> (ItemOutputBusBlockEntity) bus)
                .collect(Collectors.toList());

        ItemStack remaining = stack.copy();
        for (ItemOutputBusBlockEntity bus : itemBusses) {
            if (remaining.isEmpty()) break;
            ItemStackHandler handler = bus.getItemHandler();
            
            for (int i = 0; i < handler.getSlots(); i++) {
                if (remaining.isEmpty()) break;
                ItemStack existing = handler.getStackInSlot(i);
                
                if (existing.isEmpty()) {
                    int toInsert = Math.min(remaining.getCount(), Math.min(handler.getSlotLimit(i), remaining.getMaxStackSize()));
                    handler.setStackInSlot(i, remaining.copyWithCount(toInsert));
                    remaining.shrink(toInsert);
                } else if (ItemStack.isSameItemSameComponents(existing, remaining)) {
                    int maxStack = Math.min(handler.getSlotLimit(i), remaining.getMaxStackSize());
                    int canInsert = maxStack - existing.getCount();
                    if (canInsert > 0) {
                        int toInsert = Math.min(canInsert, remaining.getCount());
                        handler.setStackInSlot(i, existing.copyWithCount(existing.getCount() + toInsert));
                        remaining.shrink(toInsert);
                    }
                }
            }
        }
    }

    private void insertFluid(FluidStack stack, List<AbstractBusBlockEntity> outputBusses) {
        List<FluidOutputBusBlockEntity> fluidBusses = outputBusses.stream()
                .filter(bus -> bus.getBusType() == BusType.FLUID)
                .map(bus -> (FluidOutputBusBlockEntity) bus)
                .collect(Collectors.toList());

        int remaining = stack.getAmount();
        for (FluidOutputBusBlockEntity bus : fluidBusses) {
            if (remaining <= 0) break;
            FluidStack existing = bus.getFluidTank().getFluid();
            if (existing.isEmpty()) {
                bus.getFluidTank().fill(new FluidStack(stack.getFluid(), remaining), IFluidHandler.FluidAction.EXECUTE);
                remaining = 0;
            } else if (existing.isFluidEqual(stack)) {
                int canInsert = bus.getFluidTank().getCapacity() - existing.getAmount();
                int toInsert = Math.min(canInsert, remaining);
                bus.getFluidTank().fill(new FluidStack(stack.getFluid(), toInsert), IFluidHandler.FluidAction.EXECUTE);
                remaining -= toInsert;
            }
        }
    }

    public ReadOnlyEnergyStorage getEnergyStorageReadOnly(Direction direction) {
        if (this.getEnergyStorage() == null) return null;
        return new ReadOnlyEnergyStorage(this.getEnergyStorage());
    }

    public ReadOnlyItemHandler getItemHandlerReadOnly(Direction direction) {
        if (this.getItemHandler() == null) return null;
        return new ReadOnlyItemHandler(this.getItemHandler());
    }

    public ReadOnlyFluidHandler getFluidHandlerReadOnly(Direction direction) {
        if (this.getFluidHandler() == null) return null;
        return new ReadOnlyFluidHandler(this.getFluidHandler());
    }

    @Override
    public UpgradeItemHandler getUpgradeItemHandler() {
        return upgradeItemHandler;
    }

    @Override
    public Set<ResourceKey<UpgradeType>> getSupportedUpgrades() {
        return SUPPORTED_UPGRADES;
    }

    @Override
    public boolean hasUpgrade(ResourceKey<UpgradeType> upgrade) {
        for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
            ItemStack stack = this.getUpgradeItemHandler().getStackInSlot(i);
            if (stack.getItem() instanceof UpgradeItem upgradeItem && upgradeItem.getUpgradeTypeKey().equals(upgrade)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getUpgradeAmount(ResourceKey<UpgradeType> upgrade) {
        int amount = 0;

        for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
            ItemStack stackInSlot = this.getUpgradeItemHandler().getStackInSlot(i);
            if (stackInSlot.getItem() instanceof UpgradeItem upgradeItem && upgradeItem.getUpgradeTypeKey().equals(upgrade)) {
                amount += stackInSlot.getCount();
            }
        }

        return amount;
    }

    @Override
    public void onUpgradeAdded(ResourceKey<UpgradeType> upgrade) {

    }

    @Override
    public void onUpgradeRemoved(ResourceKey<UpgradeType> upgrade) {

    }

    @Override
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> blockCapability) {
        return Map.of();
    }

    @Override
    public MultiblockData getMultiblockData() {
        return this.multiblockData;
    }

    @Override
    public void setMultiblockData(MultiblockData multiblockData) {
        this.multiblockData = multiblockData;
        this.setChanged();
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider registries) {
        if (this.getMultiblockData() != null) {
            tag.put("multiblock_data", this.saveMBData());
        }

        CompoundTag compoundTag = this.upgradeItemHandler.serializeNBT(registries);
        tag.put("upgrades", compoundTag);

        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
        tag.putInt("energyPerTick", energyPerTick);
        tag.putBoolean("isProcessing", isProcessing);
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("multiblock_data")) {
            this.multiblockData = this.loadMBData(tag.getCompound("multiblock_data"));
        }

        if (tag.contains("upgrades")) {
            this.upgradeItemHandler.deserializeNBT(registries, tag.getCompound("upgrades"));
        }

        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
        energyPerTick = tag.getInt("energyPerTick");
        isProcessing = tag.getBoolean("isProcessing");
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Planet Simulator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PlanetSimulatorMenu(i, inventory, this);
    }
}
