package cofh.thermal.core.common.fluid;

import cofh.lib.common.fluid.FluidCoFH;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static cofh.lib.util.Utils.itemProperties;
import static cofh.thermal.core.ThermalCore.*;
import static cofh.thermal.core.init.registries.ThermalCreativeTabs.toolsTab;
import static cofh.thermal.lib.util.ThermalIDs.ID_FLUID_REFINED_FUEL;

public class RefinedFuelFluid extends FluidCoFH {

    private static RefinedFuelFluid INSTANCE;

    public static RefinedFuelFluid instance() {

        if (INSTANCE == null) {
            INSTANCE = new RefinedFuelFluid();
        }
        return INSTANCE;
    }

    protected RefinedFuelFluid() {

        super(FLUIDS, ID_FLUID_REFINED_FUEL);

        bucket = toolsTab(1000, ITEMS.register(bucket(ID_FLUID_REFINED_FUEL), () -> new BucketItem(stillFluid, itemProperties().craftRemainder(Items.BUCKET).stacksTo(1))));
    }

    @Override
    protected ForgeFlowingFluid.Properties fluidProperties() {

        return new ForgeFlowingFluid.Properties(type(), stillFluid, flowingFluid).bucket(bucket);
    }

    @Override
    protected Supplier<FluidType> type() {

        return TYPE;
    }

    public static final RegistryObject<FluidType> TYPE = FLUID_TYPES.register(ID_FLUID_REFINED_FUEL, () -> new FluidType(FluidType.Properties.create()
            .density(750)
            .viscosity(800)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)) {

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {

            consumer.accept(new IClientFluidTypeExtensions() {

                private static final ResourceLocation
                        STILL = new ResourceLocation("thermal:block/fluids/refined_fuel_still"),
                        FLOW = new ResourceLocation("thermal:block/fluids/refined_fuel_flow");

                @Override
                public ResourceLocation getStillTexture() {

                    return STILL;
                }

                @Override
                public ResourceLocation getFlowingTexture() {

                    return FLOW;
                }

            });
        }
    });

}
