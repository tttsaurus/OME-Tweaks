This mod aims to add a bunch of configurable QoL tweaks to other mods.

## Latest Features
- **JEI**
  - Support modifications to jei category icons
- **Industrial Foregoing**
  - Add the harvest level and blacklist to Industrial Foregoing's infinity drill
  - Make petrified fuel generator more configurable
    - Support custom fuels
    - Support tweaking existing fuels
    - Optional jei page overhaul
- **SCP Lockdown**
  - Add config to disable several scp capabilities
- **In World Crafting**
  - Add support of i18n to jei pages
- **Thermal Foundation**
  - Add config to disable default fluid interactions (works with [Fluid-Interaction-Tweaker](https://github.com/tttsaurus/Fluid-Interaction-Tweaker)'s jei compat)
- **Extra Utilities**
  - Add compat to mining node so it can detect fluid interactions from [Fluid-Interaction-Tweaker](https://github.com/tttsaurus/Fluid-Interaction-Tweaker)

## Dependency
- Mixinbooter 10.0+

## Config
Here's an example/snapshot of the config file.
Everything is set to `false` by default.
```
# Configuration file

general {
    # Enable OME Tweaks [default: false]
    B:Enable=true

    jei {
        # Enable JEI Module / Whether mixins will be loaded [default: false]
        B:Enable=true

        category_order {
            # Enable JEI Category Order [default: false]
            B:Enable=true

            # A list of jei category uids that determines the in-game jei displaying order [default: ]
            S:"JEI Category Order" <
                ORE_WASHER
                ORE_FERMENTER
                ORE_SIEVE
                tconstruct.alloy
                tconstruct.smeltery
                tconstruct.casting_table
                tconstruct.dryingrack
                mctsmelteryio:casting_machine
             >
        }
...
```

## Credits
This mod is created using [GregTechCEu's Buildscripts](https://github.com/GregTechCEu/Buildscripts)
