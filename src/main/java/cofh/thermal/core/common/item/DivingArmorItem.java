package cofh.thermal.core.common.item;

import cofh.core.client.renderer.entity.model.ArmorFullSuitModel;
import cofh.core.common.item.ArmorItemCoFH;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static cofh.lib.util.helpers.StringHelper.getTextComponent;
import static net.minecraftforge.common.ForgeMod.SWIM_SPEED;

public class DivingArmorItem extends ArmorItemCoFH {

    protected static final double[] SWIM_SPEED_BONUS = new double[]{0.60D, 0.30D, 0.10D, 0.0D};
    protected static final int AIR_DURATION = 1800;

    private Multimap<Attribute, AttributeModifier> armorAttributes;

    public DivingArmorItem(ArmorMaterial pMaterial, ArmorItem.Type pType, Item.Properties pProperties) {

        super(pMaterial, pType, pProperties);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> multimap = ImmutableMultimap.builder();
        armorAttributes = multimap.build();
    }

    public void setup() {

        ImmutableMultimap.Builder<Attribute, AttributeModifier> multimap = ImmutableMultimap.builder();
        multimap.putAll(super.getDefaultAttributeModifiers(getType().getSlot()));
        multimap.put(SWIM_SPEED.get(), new AttributeModifier(UUID_SWIM_SPEED[getType().getSlot().getIndex()], "Swim Speed", SWIM_SPEED_BONUS[getType().getSlot().getIndex()], AttributeModifier.Operation.ADDITION));
        armorAttributes = multimap.build();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        if (getType().getSlot() == EquipmentSlot.HEAD) {
            tooltip.add(getTextComponent("info.thermal.diving_helmet").withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {

        return slot == getType().getSlot() ? armorAttributes : ImmutableMultimap.of();
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {

        if (getType().getSlot() == EquipmentSlot.HEAD) {
            if (player.getAirSupply() < player.getMaxAirSupply() && world.random.nextInt(5) > 0) {
                player.setAirSupply(player.getAirSupply() + 1);
            }
            // TODO: Revisit
            //            if (!player.areEyesInFluid(FluidTags.WATER)) {
            //                Utils.addPotionEffectNoEvent(player, new EffectInstance(Effects.WATER_BREATHING, AIR_DURATION, 0, false, false, true));
            //            }
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {

        consumer.accept(new IClientItemExtensions() {

            @Override
            @Nonnull
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {

                return armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.FEET ? _default : ArmorFullSuitModel.INSTANCE.get();
            }
        });
    }

}
