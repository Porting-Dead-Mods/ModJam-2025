---
navigation:
  title: Planet Cards
  icon: modjam:planet_card
  position: 2
item_ids:
  - modjam:planet_card
  - modjam:tinted_planet_card
---

# Planet Cards

Planet cards are essential items that link the Planet Simulator to specific dimensions, enabling space missions and resource collection.

## What are Planet Cards?

<ItemLink id="modjam:planet_card" /> are dimensional link items that must be activated in their corresponding dimension before they can be used in the Planet Simulator.

## How to Get Planet Cards

Planet cards start as blank items. To link them to a planet:

1. Craft or obtain a blank planet card
2. Travel to the target dimension (Overworld, Nether, or End)
3. Right-click while holding the card
4. The card will link to the planet type for that dimension

### Priority System

- **Non-tinted cards** (regular planet cards) take priority when linking
- If only a tinted planet exists for that dimension, it will link to the tinted variant
- Each dimension can have multiple planet types

## Available Planets

### Earth (Overworld)

<ItemImage id="modjam:planet_card" components="modjam:planet={planetType:'modjam:earth',activated:true}" />

**Dimension:** Overworld

**Specialty:** Resource generation - Converts <ItemLink id="minecraft:stone" /> into ores and <ItemLink id="minecraft:coal" /> into <ItemLink id="minecraft:diamond" />

### Mars (Nether)

<ItemImage id="modjam:planet_card" components="modjam:planet={planetType:'modjam:mars',activated:true}" />

**Dimension:** Nether  

**Specialty:** Power generation - Converts <ItemLink id="minecraft:redstone" /> into energy at 500 FE/tick

### Venus (End)

**Dimension:** End

**Specialty:** Coming soon!

### Black Hole (End - Endgame)

**Dimension:** End

**Specialty:** Ultimate power generation using black hole energy

## Activating Planet Cards

Once linked, planet cards must be activated:

1. **Travel to the correct dimension** - The card shows which dimension it's linked to
2. **Right-click the card** - This activates the card
3. **Card is now ready** - Use it in the Planet Simulator

The card tooltip shows:
- **Dimension:** Where it needs to be activated
- **Status:** Activated (green) or Not Activated (red)

## Using Planet Cards

Once activated, planet cards are inserted into the Planet Simulator:

1. Place the activated planet card in the Planet Simulator's card slot
2. Supply the required input items and energy
3. The simulator will run the space mission
4. Collect output resources or generated power

## Unlinking Planet Cards

To reset a planet card back to blank:

**Craft the planet card in a shapeless recipe** - This removes the link and activation, giving you a blank card again

<Recipe id="modjam:unlink_planet_card" />

You can then re-link it to a different planet type!

## Tips

- Always activate cards in the correct dimension before use
- Different planets require different inputs (<ItemLink id="minecraft:redstone" /> for Mars, <ItemLink id="minecraft:stone" />/<ItemLink id="minecraft:coal" /> for Earth)
- Black hole cards are endgame content requiring End dimension access
- Keep multiple activated cards for different space missions
