package com.tttsaurus.ometweaks.mixins.scp;

import alexiy.secure.contain.protect.capability.bloodstone.IBloodstoneCapability;
import alexiy.secure.contain.protect.capability.cowbell.ICowbellCapability;
import alexiy.secure.contain.protect.capability.killedentities.IKilledEntitiesCapability;
import alexiy.secure.contain.protect.capability.lostitems.ILostItemsCapability;
import alexiy.secure.contain.protect.capability.shadowinfestation.IShadowInfestationCapability;
import alexiy.secure.contain.protect.capability.sleepdeprivation.ISleepDeprivationCapability;
import alexiy.secure.contain.protect.capability.zombievirus.Infection;

public class SCPNegativeCapabilities
{
    public ISleepDeprivationCapability sleepDeprivation;
    public IShadowInfestationCapability shadowInfestation;
    public IBloodstoneCapability bloodstone;
    public Infection infection;
    public IKilledEntitiesCapability killedEntities;
    public ICowbellCapability cowbell;
    public ILostItemsCapability lostItems;
}
