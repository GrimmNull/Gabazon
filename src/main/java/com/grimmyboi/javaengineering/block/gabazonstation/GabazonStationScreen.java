package com.grimmyboi.javaengineering.block.gabazonstation;


import com.grimmyboi.javaengineering.block.AbstractModScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GabazonStationScreen extends AbstractModScreen<GabazonStationContainer> {
    public GabazonStationScreen(GabazonStationContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        drawString(matrixStack, Minecraft.getInstance().font, "Money: " + menu.getMoney(), 10, 10, 0xffffff);
    }
}
