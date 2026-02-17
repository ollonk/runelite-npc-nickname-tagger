package com.npcnicknametagger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.*;

public class NpcNicknameTaggerOverlay extends Overlay
{
    private final Client client;
    private final NpcNicknameTaggerPlugin plugin;
    private final NpcNicknameTaggerConfig config;

    @Inject
    private NpcNicknameTaggerOverlay(Client client, NpcNicknameTaggerPlugin plugin, NpcNicknameTaggerConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            return null;
        }

        for (NPC npc : client.getNpcs())
        {
            if (npc == null || npc.getName() == null)
            {
                continue;
            }

            String key = npc.getName().toLowerCase();
            String replacement = plugin.getReplacements().get(key);
            if (replacement == null)
            {
                continue;
            }

            renderTile(g, npc);
            renderOverheadText(g, npc, replacement);
        }

        return null;
    }

    private void renderTile(Graphics2D g, NPC npc)
    {
        LocalPoint lp = npc.getLocalLocation();
        if (lp == null)
        {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly == null)
        {
            return;
        }

        Color fill = config.tileColor();
        g.setColor(fill);
        g.fill(poly);

        if (config.drawTileOutline())
        {
            Color outline = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), Math.min(255, fill.getAlpha() + 80));
            g.setColor(outline);
            g.draw(poly);
        }
    }

    private void renderOverheadText(Graphics2D g, NPC npc, String text)
    {
        net.runelite.api.Point p = npc.getCanvasTextLocation(g, text, npc.getLogicalHeight() + 40);
        if (p == null)
        {
            return;
        }

        Font old = g.getFont();
        g.setFont(old.deriveFont(Font.BOLD, 14f));

        if (config.textShadow())
        {
            g.setColor(new Color(0, 0, 0, 180));
            g.drawString(text, p.getX() + 1, p.getY() + 1);
        }

        g.setColor(Color.WHITE);
        g.drawString(text, p.getX(), p.getY());

        g.setFont(old);
    }
}
