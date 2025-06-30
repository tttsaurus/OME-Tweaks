package com.tttsaurus.ometweaks.integration.industrialforegoing.machine.jei;

import com.tttsaurus.ometweaks.function.IFunc_1Param;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.IMachineGuiContainer;
import com.tttsaurus.ometweaks.integration.jei.IJEIExclusionAreaGuiPiece;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import java.awt.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class MachineJEIExclusionHandler implements IAdvancedGuiHandler<BasicTeslaGuiContainer>
{
    private static boolean isFuncsInit = false;
    private static IFunc_1Param<Integer, GuiContainer> guiLeft;
    private static IFunc_1Param<Integer, GuiContainer> guiTop;

    private static void initFuncs()
    {
        if (!isFuncsInit)
        {
            isFuncsInit = true;

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            Field guiLeft = null;
            try
            {
                guiLeft = GuiContainer.class.getDeclaredField("guiLeft");
            }
            catch (Exception ignored)
            {
                try
                {
                    guiLeft = GuiContainer.class.getDeclaredField("field_147003_i");
                }
                catch (Exception ignored2) { }
            }

            try
            {
                guiLeft.setAccessible(true);
                MethodHandle handle = lookup.unreflectGetter(guiLeft);
                MachineJEIExclusionHandler.guiLeft = (arg0) ->
                {
                    try
                    {
                        return (Integer)handle.invoke(arg0);
                    }
                    catch (Throwable ignored) { return null; }
                };
            }
            catch (Exception ignored) { }

            Field guiTop = null;
            try
            {
                guiTop = GuiContainer.class.getDeclaredField("guiTop");
            }
            catch (Exception ignored)
            {
                try
                {
                    guiTop = GuiContainer.class.getDeclaredField("field_147009_r");
                }
                catch (Exception ignored2) { }
            }

            try
            {
                guiTop.setAccessible(true);
                MethodHandle handle = lookup.unreflectGetter(guiTop);
                MachineJEIExclusionHandler.guiTop = (arg0) ->
                {
                    try
                    {
                        return (Integer)handle.invoke(arg0);
                    }
                    catch (Throwable ignored) { return null; }
                };
            }
            catch (Exception ignored) { }
        }
    }

    @Override
    public Class<BasicTeslaGuiContainer> getGuiContainerClass()
    {
        return BasicTeslaGuiContainer.class;
    }

    @Override
    public List<Rectangle> getGuiExtraAreas(BasicTeslaGuiContainer guiContainer)
    {
        initFuncs();

        List<Rectangle> list = new ArrayList<>();

        int left = guiLeft.invoke(guiContainer);
        int top = guiTop.invoke(guiContainer);

        IMachineGuiContainer machineGuiContainer = (IMachineGuiContainer)guiContainer;

        for (IGuiContainerPiece piece: machineGuiContainer.getGuiPieces())
        {
            if (piece instanceof IJEIExclusionAreaGuiPiece jeiExclusionAreaGuiPiece)
            {
                Rectangle rectangle = new Rectangle(
                        left + jeiExclusionAreaGuiPiece.exclusionAreaX(),
                        top + jeiExclusionAreaGuiPiece.exclusionAreaY(),
                        jeiExclusionAreaGuiPiece.exclusionAreaWidth(),
                        jeiExclusionAreaGuiPiece.exclusionAreaHeight());
                list.add(rectangle);
            }
        }

        return list;
    }
}
