package clickmoveoptions;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("clickMoveOptionsConfig")
public interface ClickMoveOptionsConfig extends Config
{
	@ConfigItem(
            keyName = "allowleftclick",
            name = "Left Click Move",
            description = "Enables moving via left clicking the ground",
            position = 1
    )
    default boolean allowLeftClick() {
        return false;
    }

    @ConfigItem(
            keyName = "allowtileclick",
            name = "Left Click Marked Tiles",
            description = "Enables/disables moving to tiles marked via the Ground Markers plugin on left clicks",
            position = 2
    )
    default boolean allowTileClick() { return true; }

    @ConfigItem(
            keyName = "allowmiddleclick",
            name = "Middle Click Move",
            description = "Enables moving via the middle clicks " +
                    "(middle click camera rotation must be deactivated in Game Settings)",
            position = 3
    )
    default boolean allowMiddleClick() { return false; }

    @ConfigItem(
            keyName = "allowrightclick",
            name = "Right Click Move",
            description = "Enables moving via the right click menu",
            position = 4
    )
    default boolean allowRightClick() {
        return true;
    }

    @ConfigItem(
            keyName = "allowshiftclick",
            name = "Shift Click Move",
            description = "Enables moving via holding Shift + Left click",
            position = 5
    )
    default boolean allowShiftClick() {
        return false;
    }

    @ConfigItem(
            keyName = "hidewalkhere",
            name = "Hide \"Walk Here\"",
            description = "Removes the \"Walk Here\" text on right-click Menus",
            position = 6
    )
    default boolean hideWalkHere() {
        return false;
    }

    @ConfigItem(
            keyName = "hideexamine",
            name = "Hide Examine",
            description = "Removes the Examine options on right-click Menus",
            position = 7
    )
    default boolean hideExamine() {
        return false;
    }

    @ConfigItem(
            keyName = "hideCancel",
            name = "Hide Cancel",
            description = "Removes the Cancel option on right-click Menus (Menus close when you move the cursor off of them)",
            position = 8
    )
    default boolean hideCancel() {
        return false;
    }

    @ConfigItem(
            keyName = "clickThruItem",
            name = "Click-through Items",
            description = "Clicking on a ground item moves to the square instead of picking the item up",
            position = 9
    )
    default boolean doIgnoreItems() { return false; }

    @ConfigItem(
            keyName = "clickThruNPCs",
            name = "Click-through NPCs",
            description = "Clicking on a NPC moves to the square instead of interacting",
            position = 10
    )
    default boolean doIgnoreNPCs() { return false; }

    @ConfigItem(
            keyName = "clickThruObjects",
            name = "Click-through Objects",
            description = "Clicking on an object moves to the square instead of interacting",
            position = 12
    )
    default boolean doIgnoreObjects() { return false; }
}
