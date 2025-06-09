package com.tttsaurus.ometweaks.integration.fluidintetweaker;

import com.tttsaurus.fluidintetweaker.common.core.WorldIngredient;
import com.tttsaurus.fluidintetweaker.common.core.interaction.ComplexOutput;
import com.tttsaurus.fluidintetweaker.common.impl.interaction.FluidInteractionRecipeManager;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class FITInternalMethods
{
    public IFunc_1Param<Boolean, WorldIngredient> FluidInteractionRecipeManager$ingredientAExists;
    public IFunc_1Param<Boolean, WorldIngredient> FluidInteractionRecipeManager$ingredientBExists;
    public IFunc_2Param<Boolean, WorldIngredient, WorldIngredient> FluidInteractionRecipeManager$recipeExists;
    public IFunc_2Param<ComplexOutput, WorldIngredient, WorldIngredient> FluidInteractionRecipeManager$getRecipeOutput;

    public FITInternalMethods()
    {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try
        {
            Method method = FluidInteractionRecipeManager.class.getDeclaredMethod("ingredientAExists", WorldIngredient.class);
            method.setAccessible(true);
            MethodHandle handle = lookup.unreflect(method);
            FluidInteractionRecipeManager$ingredientAExists = (arg0) ->
            {
                try
                {
                    return (Boolean) handle.invoke(arg0);
                }
                catch (Throwable e) { return false; }
            };
        }
        catch (Exception ignored) { }

        try
        {
            Method method = FluidInteractionRecipeManager.class.getDeclaredMethod("ingredientBExists", WorldIngredient.class);
            method.setAccessible(true);
            MethodHandle handle = lookup.unreflect(method);
            FluidInteractionRecipeManager$ingredientBExists = (arg0) ->
            {
                try
                {
                    return (Boolean) handle.invoke(arg0);
                }
                catch (Throwable e) { return false; }
            };
        }
        catch (Exception ignored) { }

        try
        {
            Method method = FluidInteractionRecipeManager.class.getDeclaredMethod("recipeExists", WorldIngredient.class, WorldIngredient.class);
            method.setAccessible(true);
            MethodHandle handle = lookup.unreflect(method);
            FluidInteractionRecipeManager$recipeExists = (arg0, arg1) ->
            {
                try
                {
                    return (Boolean) handle.invoke(arg0, arg1);
                }
                catch (Throwable e) { return false; }
            };
        }
        catch (Exception ignored) { }

        try
        {
            Method method = FluidInteractionRecipeManager.class.getDeclaredMethod("getRecipeOutput", WorldIngredient.class, WorldIngredient.class);
            method.setAccessible(true);
            MethodHandle handle = lookup.unreflect(method);
            FluidInteractionRecipeManager$getRecipeOutput = (arg0, arg1) ->
            {
                try
                {
                    return (ComplexOutput) handle.invoke(arg0, arg1);
                }
                catch (Throwable e) { return null; }
            };
        }
        catch (Exception ignored) { }
    }
}
