package com.teampolymer.polymer.core.client.renderer;

import com.mojang.blaze3d.vertex.IVertexBuilder;


public abstract class WrappedVertexBuilder implements IVertexBuilder {
    protected final IVertexBuilder delegate;
    public WrappedVertexBuilder(IVertexBuilder pBuilder) {
        delegate = pBuilder;
    }

    @Override
    public IVertexBuilder vertex(double pX, double pY, double pZ) {
        return delegate.vertex(pX, pY, pZ);
    }

    @Override
    public IVertexBuilder color(int pRed, int pGreen, int pBlue, int pAlpha) {
        return delegate.color(pRed, pGreen, pBlue, pAlpha);
    }

    @Override
    public IVertexBuilder uv(float pU, float pV) {
        return delegate.uv(pU, pV);
    }

    @Override
    public IVertexBuilder overlayCoords(int pU, int pV) {
        return delegate.overlayCoords(pU, pV);
    }

    @Override
    public IVertexBuilder uv2(int pU, int pV) {
        return delegate.uv2(pU, pV);
    }

    @Override
    public IVertexBuilder normal(float pX, float pY, float pZ) {
        return delegate.normal(pX, pY, pZ);
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
