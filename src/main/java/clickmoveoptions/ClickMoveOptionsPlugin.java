package clickmoveoptions;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.MenuOptionClicked;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Red Clicks Only Test"
)
public class ClickMoveOptionsPlugin extends Plugin
{
	@Inject
	public Client client;

	@Inject
	private ClickMoveOptionsConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private Gson gson;

	@Provides
	ClickMoveOptionsConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ClickMoveOptionsConfig.class);
	}

	/**
	 * Ignores "select" clicks (left, middle, ...) depending on user preferences
	 * @param event Click event
	 */
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event) {
		if (event.getMenuAction() == MenuAction.WALK && isClickApplicable()) {
			event.consume(); // Ignore the click
		}
	}

	/**
	 * Confirms that Walk event should be blocked depending on the current plugin settings
	 * @return true if the event should be consumed
	 */
	private boolean isClickApplicable() {
		if(client.isMenuOpen()) { // Right clicks
			return !config.allowRightClick();
		}

		else { // Left clicks
			if(client.isKeyPressed(KeyCode.KC_SHIFT)) { // Shift + Click
				return !config.allowShiftClick();
			}

			if(client.getMouseCurrentButton() == 4) { //Mousewheel Click
				return !config.allowMiddleClick();
			}

			if(config.allowLeftClick() != config.allowTileClick()) {//Left Click on Tile
				if(isTileMarked(client.getSelectedSceneTile())) {
					return !config.allowTileClick();
				}
			}

			return !config.allowLeftClick();
		}
	}

	/**
	 * Removes specific options from right-click menus per specifications
	 * set in the config to reduce mis-clicks
	 */
	@Subscribe
	public void onMenuOpened(MenuOpened event) {
		if(config.hideWalkHere() || config.hideCancel() || config.hideExamine()) {
			MenuEntry[] entries = event.getMenuEntries();
			List<MenuEntry> updatedEntries = new ArrayList<>(entries.length);

			for(MenuEntry entry : entries) {
				if(config.hideWalkHere() && entry.getType() == MenuAction.WALK) { continue; }
				if(config.hideCancel() && entry.getType() == MenuAction.CANCEL) { continue; }
				if(config.hideExamine() && (
						entry.getType() == MenuAction.EXAMINE_ITEM_GROUND ||
								entry.getType() == MenuAction.EXAMINE_NPC ||
								entry.getType() == MenuAction.EXAMINE_OBJECT)) { continue; }

				updatedEntries.add(entry);
			}

			if(updatedEntries.size() != entries.length) {
				client.getMenu().setMenuEntries(updatedEntries.toArray(new MenuEntry[0]));
			}
		}
	}

	/**
	 * Deprioritizes (removes) left-click options from user-selected groups, so that movement is the default
	 * @param event The "creation" of each menu item
	 */
	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event) {
		if(config.doIgnoreItems()) {
			MenuAction type = event.getMenuEntry().getType();
			if(type == MenuAction.GROUND_ITEM_FIRST_OPTION || type == MenuAction.GROUND_ITEM_SECOND_OPTION ||
					type == MenuAction.GROUND_ITEM_THIRD_OPTION || type == MenuAction.GROUND_ITEM_FOURTH_OPTION ||
					type == MenuAction.GROUND_ITEM_FIFTH_OPTION || type == MenuAction.WIDGET_TARGET_ON_GROUND_ITEM) {
				event.getMenuEntry().setDeprioritized(true);
			}
		}

		if(config.doIgnoreNPCs()) {
			MenuAction type = event.getMenuEntry().getType();
			if(type == MenuAction.NPC_FIRST_OPTION || type == MenuAction.NPC_SECOND_OPTION ||
					type == MenuAction.NPC_THIRD_OPTION || type == MenuAction.NPC_FOURTH_OPTION ||
					type == MenuAction.NPC_FIFTH_OPTION || type == MenuAction.WIDGET_TARGET_ON_NPC) {
				event.getMenuEntry().setDeprioritized(true);
			}
		}

		if(config.doIgnoreObjects()) {
			MenuAction type = event.getMenuEntry().getType();
			if(type == MenuAction.GAME_OBJECT_FIRST_OPTION || type == MenuAction.GAME_OBJECT_SECOND_OPTION ||
					type == MenuAction.GAME_OBJECT_THIRD_OPTION || type == MenuAction.GAME_OBJECT_FOURTH_OPTION ||
					type == MenuAction.GAME_OBJECT_FIFTH_OPTION || type == MenuAction.WIDGET_TARGET_ON_GAME_OBJECT) {
				event.getMenuEntry().setDeprioritized(true);
			}
		}
	}

	/**
	 * Retrieves the marked tiles in the Ground Markers plugin and checks it against our clicked Tile
	 * Source: GroundMarkers. Credit to Adam, Jordan Atwood, Weber, and takuyakanbr
	 * @param target Tile clicked
	 * @return Whether the tile is marked
	 */
	boolean isTileMarked(Tile target)
	{
		if(target == null) { return false; }

		WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, target.getLocalLocation());
		int regionId = worldPoint.getRegionID();
		GroundMarkerPoint point = new GroundMarkerPoint(regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), null, null);

		String json = configManager.getConfiguration("groundMarker", "region_" + regionId);
		if (Strings.isNullOrEmpty(json)) { return false; }

		List<GroundMarkerPoint> groundMarkerPoints = new ArrayList<>(gson.fromJson(json, new TypeToken<List<GroundMarkerPoint>>(){}.getType()));
        return groundMarkerPoints.contains(point);
    }
}