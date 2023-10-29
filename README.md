# XP Curver
A Fabric/Quilt mod that allows the Minecraft exp level progression
to be tweaked.
Default configuration values are set to Minecraft's defaults.
Works both server and client side.


### Configuration
Currently there are three experience brackets:
- levels 1 to 15
- levels 16 to 30
- levels 31+

The formula is defined as the amount of xp it takes to level up 1 level
**__to__** the level in that bracket. So the first bracket is n=0 to 14,
the second bracket is n=15 to 29, etc.


Supported features in the configuration formula:
- The basic four operations
- Exponents
- Order of operations and parentheses
- Decimals
- The variable "level" defined as the player's current exp level.

Negative constants are not supported, and there are no checks to prevent
integer overflow.


### Requirements
- Minecraft 1.20.x
- Fabric API 0.90.x or Quilt equivalent
- owo-lib 0.11.x

Also compatible with Mod Menu 7.x. Note that any changes to
configuration will require a restart.
 
