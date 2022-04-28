package com.teampolymer.polymer.core.client.renderer;

import com.mojang.blaze3d.vertex.IVertexBuilder;

public class ColoredVertexBuilder extends WrappedVertexBuilder {
    private final float redFactor;
    private final float greenFactor;
    private final float blueFactor;
    private final float alphaFactor;

    public ColoredVertexBuilder(IVertexBuilder pBuilder, float pRedFactor, float pGreenFactor, float pBlueFactor, float pAlphaFactor) {
        super(pBuilder);
        this.redFactor = pRedFactor;
        this.greenFactor = pGreenFactor;
        this.blueFactor = pBlueFactor;
        this.alphaFactor = pAlphaFactor;
    }

    @Override
    public void vertex(float pX, float pY, float pZ, float pRed, float pGreen, float pBlue, float pAlpha, float pTexU, float pTexV, int pOverlayUV, int pLightmapUV, float pNormalX, float pNormalY, float pNormalZ) {
        pRed *= redFactor;
        pGreen *= greenFactor;
        pBlue *= blueFactor;
        pAlpha *= alphaFactor;
        super.vertex(pX, pY, pZ, pRed, pGreen, pBlue, pAlpha, pTexU, pTexV, pOverlayUV, pLightmapUV, pNormalX, pNormalY, pNormalZ);
    }

    @Override
    public IVertexBuilder color(int pRed, int pGreen, int pBlue, int pAlpha) {
        pRed = (int) (pRed * redFactor);
        pGreen = (int) (pGreen * greenFactor);
        pBlue = (int) (pBlue * blueFactor);
        return super.color(pRed, pGreen, pBlue, pAlpha);
    }

}
