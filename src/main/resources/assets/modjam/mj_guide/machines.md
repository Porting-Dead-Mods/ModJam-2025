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

A 7x7x3 multiblock that runs space missions to collect resources.

### Structure

<GameScene zoom="3" interactive={true} fullWidth={true}>
  <MultiblockShape multiblock="modjam:planet_simulator" formed={true} unformed={true} direction="west"> </MultiblockShape>
</GameScene>

**Components:**
- <ItemLink id="modjam:planet_simulator_controller" /> (1x)
- <ItemLink id="modjam:planet_simulator_frame" /> (structure outline)
- <ItemLink id="modjam:planet_simulator_casing" /> (walls - can be replaced with buses)
- Input/Output buses (see **Buses** page)

### How It Works

1. Insert an activated <ItemLink id="modjam:planet_card" />
2. Supply input items via <ItemLink id="modjam:item_input_bus" />
3. Provide energy via <ItemLink id="modjam:energy_input_bus" />
4. Collect outputs from <ItemLink id="modjam:item_output_bus" /> or <ItemLink id="modjam:energy_output_bus" />

Different planets accept different inputs - check JEI for all recipes.

### Upgrades

Install upgrades to boost performance:
- <ItemLink id="modjam:upgrade_speed" /> - Faster processing
- <ItemLink id="modjam:upgrade_energy" /> - Lower energy cost
- <ItemLink id="modjam:upgrade_luck" /> - Bonus output chance

---

## Compressor

Creates compressed materials from ingots.

<ItemImage id="modjam:compressor" />

Connect to power, insert <ItemLink id="modjam:tantalum_ingot" />, receive <ItemLink id="modjam:tantalum_sheet" />.

<Recipe id="modjam:tantalum_plate_compressing" />

Supports the same upgrade system as the Planet Simulator.
