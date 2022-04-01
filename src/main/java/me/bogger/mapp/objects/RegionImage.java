package me.bogger.mapp.objects;

public class RegionImage {
    private final byte[] data;
    private final String name;

    public RegionImage(byte[] data,
                       String name) {
        this.data = data;
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }
}
