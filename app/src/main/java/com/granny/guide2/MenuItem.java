package com.granny.guide2;

/**
 * The {@link MenuItem} class.
 * <p>Defines the attributes for a restaurant menu item.</p>
 */
class MenuItem {

    private final String name;
    private final String imageName;
    private final String video;

    public MenuItem(String name, String imageName, String video) {
        this.name = name;
        this.imageName = imageName;
        this.video = video;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public String getVideo() {
        return video;
    }
}
