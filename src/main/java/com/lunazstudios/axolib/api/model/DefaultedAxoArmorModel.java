package com.lunazstudios.axolib.api.model;

import com.lunazstudios.axolib.api.animatable.AxoArmor;
import net.minecraft.resources.Identifier;

/**
 * Resolves armor model and texture paths by convention:
 * <ul>
 *   <li>Model:   {@code assets/<namespace>/axolib/models/armor/<name>.axo.json}</li>
 *   <li>Texture: {@code assets/<namespace>/textures/armor/<name>.png}</li>
 * </ul>
 */
public class DefaultedAxoArmorModel<T extends AxoArmor> extends DefaultedAxoModel<T> {
    public DefaultedAxoArmorModel(Identifier baseResource) {
        super(baseResource, "armor");
    }
}
