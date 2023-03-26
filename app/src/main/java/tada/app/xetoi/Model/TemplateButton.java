package tada.app.xetoi.Model;

import android.graphics.Bitmap;

public class TemplateButton {
    private String name;
    private int image;

    public TemplateButton() {}

    public TemplateButton(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
