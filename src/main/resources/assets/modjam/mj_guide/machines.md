---
navigation:
  title: Machines
  icon: modjam:planet_simulator_controller
  position: 3
item_ids:
  - modjam:planet_simulator_controller
  - modjam:planet_simulator_frame
  - modjam:planet_simulator_casing
  - modjam:compressor
---

# Machines

## Planet Simulator

The **Planet Simulator** is a multiblock machine that conducts space missions to collect resources and generate power from planetary environments.

### Structure

The Planet Simulator is a 7x7x3 multiblock structure. Here's how to build it:

<GameScene zoom="3" interactive={true} fullWidth={true}>
  <MultiblockShape multiblock="modjam:planet_simulator" formed={true} unformed={true} direction="west"> </MultiblockShape>
</GameScene>

**Required Blocks:**
- **Controller:** <ItemLink id="modjam:planet_simulator_controller" /> (1x)
- **Frame:** <ItemLink id="modjam:planet_simulator_frame" /> blocks (outline)
- **Casing:** <ItemLink id="modjam:planet_simulator_casing" /> or Bus blocks (walls)
- **Buses:** <ItemLink id="modjam:energy_input_bus" />, <ItemLink id="modjam:item_input_bus" />, <ItemLink id="modjam:fluid_input_bus" /> for inputs
- **Output Buses:** <ItemLink id="modjam:energy_output_bus" />, <ItemLink id="modjam:item_output_bus" />, <ItemLink id="modjam:fluid_output_bus" /> for outputs

### How It Works

The Planet Simulator runs space missions that:
- **Consume:** Input items + Energy
- **Produce:** Output resources or energy

1. Build the 7x7x3 multiblock structure
2. Add buses for item/energy/fluid input and output
3. Insert an activated planet card
4. Supply the required input items and energy
5. The mission runs and produces resources

### Space Missions

Each planet card enables different missions:

**Earth Missions:**
- <ItemLink id="minecraft:stone" /> (16x) → Various ores and materials (200 FE/tick, 400 ticks)
- <ItemLink id="minecraft:coal" /> (4x) → <ItemLink id="minecraft:diamond" /> with 50% chance (100 FE/tick, 200 ticks)

**Mars Missions:**
- <ItemLink id="minecraft:redstone" /> (1x) → 500 FE/tick power generation (100 ticks)

**Black Hole (Endgame):**
- Ultimate power generation (requires End dimension access)

### Planet Cards

Learn how to get and activate planet cards in the **Planet Cards** chapter!

<ItemImage id="modjam:planet_card" />

---

## Compressor

The **Compressor** is a machine that compresses materials into sheets.

### Basic Info

<ItemImage id="modjam:compressor" />

The compressor uses energy to compress materials. It's essential for creating tantalum sheets needed for multiblock construction.

### Compression Process

<ItemImage id="modjam:tantalum_sheet" scale="1.5" />

The compressor creates compressed materials like tantalum sheets from tantalum ingots.

### Using the Compressor

1. Connect the compressor to a power source
2. Place materials (like <ItemLink id="modjam:tantalum_ingot" />) in the input slot
3. The machine compresses them and outputs <ItemLink id="modjam:tantalum_sheet" />
4. Add <ItemLink id="modjam:upgrade_speed" /> or <ItemLink id="modjam:upgrade_energy" /> to improve performance

### Recipe

<Recipe id="modjam:tantalum_plate_compressing" />

---

## Buses

Buses are essential for connecting the Planet Simulator to your power and storage systems. Learn more in the **Buses** section!
