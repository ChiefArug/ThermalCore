package cofh.thermal.core.init.registries;

import com.google.common.collect.Sets;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Supplier;

import static cofh.lib.util.Constants.TRUE;
import static cofh.lib.util.constants.ModIds.ID_THERMAL;
import static cofh.thermal.core.ThermalCore.CREATIVE_TABS;
import static cofh.thermal.core.ThermalCore.ITEMS;
import static cofh.thermal.lib.util.ThermalIDs.*;

public class ThermalCreativeTabs {

    private ThermalCreativeTabs() {

    }

    public static void register() {

    }

    private static final Set<Triple<RegistryObject<Item>, Integer, Supplier<Boolean>>> BLOCKS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<Triple<RegistryObject<Item>, Integer, Supplier<Boolean>>> FOODS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<Triple<RegistryObject<Item>, Integer, Supplier<Boolean>>> DEVICES_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<Triple<RegistryObject<Item>, Integer, Supplier<Boolean>>> ITEMS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<Triple<RegistryObject<Item>, Integer, Supplier<Boolean>>> TOOLS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());

    public static RegistryObject<Item> blocksTab(RegistryObject<Item> reg) {

        return blocksTab(0, reg);
    }

    public static RegistryObject<Item> blocksTab(int order, RegistryObject<Item> reg) {

        return blocksTab(order, reg, TRUE);
    }

    public static RegistryObject<Item> blocksTab(int order, RegistryObject<Item> reg, Supplier<Boolean> flag) {

        BLOCKS_TAB.add(Triple.of(reg, order, flag));
        return reg;
    }

    public static RegistryObject<Item> devicesTab(RegistryObject<Item> reg) {

        return devicesTab(0, reg);
    }

    public static RegistryObject<Item> devicesTab(int order, RegistryObject<Item> reg) {

        return devicesTab(order, reg, TRUE);
    }

    public static RegistryObject<Item> devicesTab(int order, RegistryObject<Item> reg, Supplier<Boolean> flag) {

        DEVICES_TAB.add(Triple.of(reg, order, flag));
        return reg;
    }

    public static RegistryObject<Item> foodsTab(RegistryObject<Item> reg) {

        return foodsTab(0, reg);
    }

    public static RegistryObject<Item> foodsTab(int order, RegistryObject<Item> reg) {

        return foodsTab(order, reg, TRUE);
    }

    public static RegistryObject<Item> foodsTab(int order, RegistryObject<Item> reg, Supplier<Boolean> flag) {

        FOODS_TAB.add(Triple.of(reg, order, flag));
        return reg;
    }

    public static RegistryObject<Item> itemsTab(RegistryObject<Item> reg) {

        return itemsTab(0, reg);
    }

    public static RegistryObject<Item> itemsTab(int order, RegistryObject<Item> reg) {

        return itemsTab(order, reg, TRUE);
    }

    public static RegistryObject<Item> itemsTab(RegistryObject<Item> reg, Supplier<Boolean> flag) {

        ITEMS_TAB.add(Triple.of(reg, 0, flag));
        return reg;
    }

    public static RegistryObject<Item> itemsTab(int order, RegistryObject<Item> reg, Supplier<Boolean> flag) {

        ITEMS_TAB.add(Triple.of(reg, order, flag));
        return reg;
    }

    public static RegistryObject<Item> toolsTab(RegistryObject<Item> reg) {

        return toolsTab(0, reg);
    }

    public static RegistryObject<Item> toolsTab(int order, RegistryObject<Item> reg) {

        return toolsTab(order, reg, TRUE);
    }

    public static RegistryObject<Item> toolsTab(int order, RegistryObject<Item> reg, Supplier<Boolean> flag) {

        TOOLS_TAB.add(Triple.of(reg, order, flag));
        return reg;
    }

    private static final Comparator<Triple<RegistryObject<Item>, Integer, Supplier<Boolean>>> ORDER_COMPARISON = Comparator.comparing(Triple::getMiddle);

    private static final Comparator<RegistryObject<Item>> MOD_ID_COMPARISON = (itemA, itemB) -> {

        String modA = itemA.get().getCreatorModId(new ItemStack(itemA.get()));
        String modB = itemB.get().getCreatorModId(new ItemStack(itemB.get()));
        return modA == null || modB == null ? 0 : modA.compareTo(modB);
    };

    private static final RegistryObject<CreativeModeTab> THERMAL_BLOCKS = CREATIVE_TABS.register(ID_THERMAL + ".blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.blocks"))
            .icon(() -> new ItemStack(ITEMS.get(ID_ENDERIUM_BLOCK)))
            .displayItems((parameters, output) -> BLOCKS_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> {
                if (item.getRight().get()) output.accept(item.getLeft().get());
            }))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_DEVICES = CREATIVE_TABS.register(ID_THERMAL + ".devices", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.devices"))
            .icon(() -> new ItemStack(ITEMS.get(ID_TINKER_BENCH)))
            .displayItems((parameters, output) -> DEVICES_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> {
                if (item.getRight().get()) {
                    output.accept(item.getLeft().get());
                }
            }))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_FOODS = CREATIVE_TABS.register(ID_THERMAL + ".foods", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.foods"))
            .icon(() -> new ItemStack(ITEMS.get(ID_APPLE_BLOCK)))
            .displayItems((parameters, output) -> FOODS_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> {
                if (item.getRight().get()) {
                    output.accept(item.getLeft().get());
                }
            }))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_ITEMS = CREATIVE_TABS.register(ID_THERMAL + ".items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.items"))
            .icon(() -> new ItemStack(ITEMS.get("signalum_gear")))
            .displayItems((parameters, output) -> ITEMS_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> {
                if (item.getRight().get()) {
                    output.accept(item.getLeft().get());
                }
            }))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_TOOLS = CREATIVE_TABS.register(ID_THERMAL + ".tools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.tools"))
            .icon(() -> new ItemStack(ITEMS.get(ID_WRENCH)))
            .displayItems((parameters, output) -> TOOLS_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> {
                if (item.getRight().get()) {
                    output.accept(item.getLeft().get());
                }
            }))
            .build());

}
