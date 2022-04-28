package com.teampolymer.polymer.core.client.renderer;

import com.mojang.blaze3d.vertex.IVertexBuilder;


public abstract class WrappedVertexBuilder implements IVertexBuilder {
    protected final IVertexBuilder delegate;
    public WrappedVertexBuilder(IVertexBuilder pBuilder) {
        delegate = pBuilder;
    }

    @Override
    public IVertexBuilder vertex(double pX, double pY, double pZ) {
        delegate.vertex(pX, pY, pZ);
        return this;
    }

    @Override
    public IVertexBuilder color(int pRed, int pGreen, int pBlue, int pAlpha) {
        delegate.color(pRed, pGreen, pBlue, pAlpha);
        return this;
    }

    @Override
    public IVertexBuilder uv(float pU, float pV) {
        delegate.uv(pU, pV);
        return this;
    }

    @Override
    public IVertexBuilder overlayCoords(int pU, int pV) {
        delegate.overlayCoords(pU, pV);
        return this;
    }

    @Override
    public IVertexBuilder uv2(int pU, int pV) {
        delegate.uv2(pU, pV);
        return this;
    }

    @Override
    public IVertexBuilder normal(float pX, float pY, float pZ) {
        delegate.normal(pX, pY, pZ);
        return this;
    }

    @Override
    public void endVertex() {
        delegate.endVertex();
    }

    @Override
    public void vertex(float pX, float pY, float pZ, float pRed, float pGreen, float pBlue, float pAlpha, float pTexU, float pTexV, int pOverlayUV, int pLightmapUV, float pNormalX, float pNormalY, float pNormalZ) {
        delegate.vertex(pX, pY, pZ, pRed, pGreen, pBlue, pAlpha, pTexU, pTexV, pOverlayUV, pLightmapUV, pNormalX, pNormalY, pNormalZ);
    }


}
