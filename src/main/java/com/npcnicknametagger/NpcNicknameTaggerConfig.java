package com.npcnicknametagger;

import net.runelite.client.config.*;

@ConfigGroup("NpcNicknameTagger")
public interface NpcNicknameTaggerConfig extends Config
{
    @ConfigItem(
        keyName = "replacements",
        name = "NPC replacements",
        description = "One per line: Npc Name,NewName",
        position = 0
    )
    default String replacements()
    {
        return "";
    }

    @Alpha
    @ConfigItem(
        keyName = "tileColor",
        name = "Tile color",
        description = "Color used for the NPC tile highlight",
        position = 1
    )
    default java.awt.Color tileColor()
    {
        return new java.awt.Color(0, 255, 255, 80);
    }

    @ConfigItem(
        keyName = "drawTileOutline",
        name = "Tile outline",
        description = "Draw an outline around the tile",
        position = 2
    )
    default boolean drawTileOutline()
    {
        return true;
    }

    @ConfigItem(
        keyName = "textShadow",
        name = "Text shadow",
        description = "Draw a subtle shadow behind overhead text",
        position = 3
    )
    default boolean textShadow()
    {
        return true;
    }
}
