package com.grimmyboi.javaengineering.block.thermalgenerator;


import com.grimmyboi.javaengineering.block.AbstractModScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ThermalGeneratorScreen extends AbstractModScreen<ThermalGeneratorContainer> {
    public ThermalGeneratorScreen(ThermalGeneratorContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        drawString(matrixStack, Minecraft.getInstance().font, "Energy: " + menu.getEnergy(), 10, 10, 0xffffff);
    }
}

