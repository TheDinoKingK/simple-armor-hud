package com.armorhud.hud;

import com.armorhud.armor.ArmorAccessor;
import com.armorhud.armorHud;
import com.armorhud.config.config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class armorRenderer {
    public static void renderArmor(GuiGraphicsExtractor graphics, Minecraft minecraft, int armorHeight, int startXPosition) {
        int scaledWidth = graphics.guiWidth();

        assert minecraft.player != null;
        ArmorAccessor armorAccessor = armorHud.getArmorAccessor();
        List<ItemStack> armorPieces = armorAccessor.getArmorPieces(minecraft.player);

        final int hungerWidth = 14; // Magic number to center 4 armor pieces
        final int armorWidth = 15;

//      If we trim empty slots, check all slots if they're empty
        int emptyArmorSlots = 0;
        if ( config.TRIM_EMPTY_SLOTS ) {
            for ( ItemStack stack : armorPieces ) {
                if ( stack.isEmpty() ) {
                    emptyArmorSlots++;
                }
            }
        }

//      Magical starting positions
        float hungerX = scaledWidth / 2f + startXPosition;
        float x = hungerX + hungerWidth - (7 * emptyArmorSlots) + 2 - (armorWidth * 2);

//      Armor x position calculations
        if (config.RTL) {
            if ( config.TRIM_EMPTY_SLOTS ) {
                x -= (float) (( (float) armorWidth / 2 ) + 0.5);
            }

            if(config.NUMERIC_DURABILITY) {
                if(!config.TRIM_EMPTY_SLOTS) {
                    x += 5;
                }

                x += armorWidth;

                switch (config.position.name()) {
                    case "HOTBAR_LEFT":
                        x -= 10;
                        break;
                    case "HOTBAR_RIGHT":
                        x += 8;
                        break;
                }
            }

            x += armorWidth;
            armorPieces = armorPieces.reversed();

            for ( ItemStack armor : armorPieces ) {
                if ( config.TRIM_EMPTY_SLOTS && armor.isEmpty() ) continue;

                if(config.NUMERIC_DURABILITY) {
                    x -= 5;
                }

                x -= armorWidth;
                renderArmorPiece(graphics, minecraft, minecraft.player, x, armorHeight, armor);
            }
        } else {
            if ( config.TRIM_EMPTY_SLOTS ) {
                x += ( (float) hungerWidth / 2 );
            }

            if(config.NUMERIC_DURABILITY) {
                x += armorWidth;

                switch (config.position.name()) {
                    case "HOTBAR_LEFT":
                        x -= 10;
                        break;
                    case "HOTBAR_RIGHT":
                        x += 8;
                        break;
                }
            }

            for ( ItemStack armor : armorPieces ) {
                if (config.TRIM_EMPTY_SLOTS && armor.isEmpty()) continue;

                if(config.NUMERIC_DURABILITY) {
                    x -= 5;
                }

                x -= armorWidth;
                renderArmorPiece(graphics, minecraft, minecraft.player, x, armorHeight, armor);
            }
        }
    }

    public static void renderArmorPiece(GuiGraphicsExtractor graphics, Minecraft minecraft, Player player, float x, float y, ItemStack stack) {
        if ( stack.isEmpty() ) return;

        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);

        graphics.item(player, stack, 0, 0, 1);

        graphics.itemDecorations(minecraft.font, stack, 0,0);

        if((config.NUMERIC_DURABILITY)) {
            int armorDura = stack.getMaxDamage() - stack.getDamageValue();
            int armorDuraPercent = (int) Math.floor(((float) armorDura / stack.getMaxDamage()) * 100);
            if (armorDuraPercent == 100) {
                graphics.pose().popMatrix();
                return;
            }
            String armorText = String.valueOf(armorDura);
            int xText;
            if (config.numericFormat == config.NumFormat.PERCENT) {
                armorText = armorDuraPercent + "%";
                xText = 2 - (3*(String.valueOf(armorDuraPercent).length()-1));
            } else {
                xText = 5 - (3*(String.valueOf(armorDura).length()-1));
            }

            graphics.text(minecraft.font, armorText, xText, -8, ARGB.opaque(stack.getBarColor()), true);
        }
        graphics.pose().popMatrix();
    }
}
