# rifts
Rift config files are added under `plugins/Rifts/configs/`. Any number of config files may be added.

A full rift config file would look like this:
```
rift:
   # Unique ID
   rift-id: "rift-example"
   # Rift size specified in the main config file
   rift-size: "large"
   # Show the core explosion effect
   core-effect: true
   # Show the core secondary effects
   secondary-effect: true
   # Show ambient effects that spawn on the ground
   ambient-effect: true
   # Max number of unique potions applied
   max-potions-applied: 2
   # List of living entities to be spawned randomly
   mobs:
     - "skeleton"
     - "zombie"
     - "husk"
     - "stray"
     - "zombie_villager"
   # List of potion effects to be randomly applied
   potion-effects:
     - "speed"
     - "fast_digging"
     - "increase_damage"
     - "damage_resistance"
   # Sounds to be played at the core
   sounds:
     # Will only play if the core particles are displayed
     core:
       - "entity_firework_large_blast:1"
       - "entity_firework_blast:1"
     # Will only play if the secondary partciles are displayed
     secondary:
       - "entity_enderdragon_fireball_explode:1"
```
