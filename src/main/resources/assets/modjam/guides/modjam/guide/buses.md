---
navigation:
  title: Buses
  icon: modjam:energy_input_bus
  position: 4
item_ids:
  - modjam:energy_input_bus
  - modjam:energy_output_bus
  - modjam:item_input_bus
  - modjam:item_output_bus
  - modjam:fluid_input_bus
  - modjam:fluid_output_bus
---

# Buses

Buses are the core component for transferring resources between machines, energy storage, and inventories.

## Types of Buses

ModJam provides three types of buses for different resource types:

### Energy Buses

Transfer energy between machines.

<Row>
  <ItemImage id="modjam:energy_input_bus" />
  <ItemImage id="modjam:energy_output_bus" />
</Row>

**Energy Input Bus**: Supplies energy to machines  
**Energy Output Bus**: Extracts excess energy from machines

### Fluid Buses

Transfer fluids between blocks.

<Row>
  <ItemImage id="modjam:fluid_input_bus" />
  <ItemImage id="modjam:fluid_output_bus" />
</Row>

**Fluid Input Bus**: Supplies fluids to machines  
**Fluid Output Bus**: Extracts fluids from machines

### Item Buses

Transfer items between inventories.

<Row>
  <ItemImage id="modjam:item_input_bus" />
  <ItemImage id="modjam:item_output_bus" />
</Row>

**Item Input Bus**: Supplies items to machines  
**Item Output Bus**: Extracts items from machines

## How to Use

1. Place the appropriate bus block on the side of a machine
2. Open the bus interface to configure input/output slots
3. Connect to neighboring blocks or pull from/push to hopper systems
4. Use the slot selector to choose which slots to transfer

## Configuration

Each bus type has multiple slots (up to 6) that can be configured independently. You can:

- Select which items/fluids/energy pass through each slot
- Set specific inventories as targets
- Prioritize certain slots over others

This allows for complex automation setups!
