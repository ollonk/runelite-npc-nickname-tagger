package com.npcnicknametagger;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class NpcNicknameTaggerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(NpcNicknameTaggerPlugin.class);
		RuneLite.main(args);
	}
}
