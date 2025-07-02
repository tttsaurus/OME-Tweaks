package com.tttsaurus.ometweaks.mixins.taiga;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.sosnitzka.taiga.proxy.ClientProxy;
import com.tttsaurus.ometweaks.integration.taiga.TAIGAModule;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientProxy.class)
public class ClientProxyMixin
{
    @WrapMethod(method = "registerBookPages", remap = false)
    public void registerBookPages(Operation<Void> original)
    {
        if (!TAIGAModule.DISABLE_TINKER_BOOK_TAIGA_PAGE)
            original.call();
    }
}
