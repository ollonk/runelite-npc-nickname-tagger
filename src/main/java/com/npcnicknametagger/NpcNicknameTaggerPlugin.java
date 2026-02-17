package com.npcnicknametagger;

import com.google.inject.Provides;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;

import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
    name = "NPC Nickname Tagger",
    description = "Display a custom nickname above NPCs and optionally highlight their tiles"
)
public class NpcNicknameTaggerPlugin extends Plugin
{
    @Inject private OverlayManager overlayManager;
    @Inject private NpcNicknameTaggerOverlay overlay;
    @Inject private NpcNicknameTaggerConfig config;

    private final Map<String, String> replacements = new ConcurrentHashMap<>();

    Map<String, String> getReplacements()
    {
        return replacements;
    }

    @Override
    protected void startUp()
    {
        rebuildMap();
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        replacements.clear();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e)
    {
        if (!"NpcTagger".equals(e.getGroup()))
        {
            return;
        }
        rebuildMap();
    }

    private void rebuildMap()
    {
        replacements.clear();

        String raw = config.replacements();
        if (raw == null || raw.trim().isEmpty())
        {
            return;
        }

        for (String line : raw.split("\\r?\\n"))
        {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#"))
            {
                continue;
            }

            int comma = line.indexOf(',');
            if (comma <= 0 || comma >= line.length() - 1)
            {
                continue;
            }

            String npcName = line.substring(0, comma).trim().toLowerCase();
            String newName = line.substring(comma + 1).trim();

            if (!npcName.isEmpty() && !newName.isEmpty())
            {
                replacements.put(npcName, newName);
            }
        }
    }

    @Provides
    NpcNicknameTaggerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(NpcNicknameTaggerConfig.class);
    }
}
