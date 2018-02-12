package io.github.mrsperry.rifts.rifts;

public enum RiftSize {
    Small(25, 300),
    Medium(50, 4200),
    Large(100, 600);

    public final int radius;
    public final int timer;

    RiftSize(int radius, int timer) {
        this.radius = radius;
        this.timer = timer;
    }
}
