package hdphoto.galleryimages.gelleryalbum.utils;


public class ShapeBuilder {
    private int currentShapeColor;
    private int currentShapeOpacity;
    private float currentShapeSize;

    public ShapeBuilder() {
        withShapeSize(25.0f);
        withShapeOpacity(255);
        withShapeColor(-16777216);
    }

    public ShapeBuilder withShapeSize(float f) {
        this.currentShapeSize = f;
        return this;
    }

    public float getShapeSize() {
        return this.currentShapeSize;
    }

    public ShapeBuilder withShapeOpacity(int i) {
        this.currentShapeOpacity = i;
        return this;
    }

    public int getShapeOpacity() {
        return this.currentShapeOpacity;
    }

    public ShapeBuilder withShapeColor(int i) {
        this.currentShapeColor = i;
        return this;
    }

    public int getShapeColor() {
        return this.currentShapeColor;
    }
}
