package clickmoveoptions;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ClickMoveOptionsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ClickMoveOptionsPlugin.class);
		RuneLite.main(args);
	}
}