package std.deviation.entities.components.renderComponents.renderParts;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class TexturePart extends RenderPart
{
    public TextureRegion region;

    public TexturePart(TextureRegion region)
    {
        this.region = region;
    }

    @Override
    public void render(Batch batch, float x, float y, float elapsed, PlayMode mode)
    {
        batch.draw(region, x - (region.getRegionWidth() / 2), y - (region.getRegionHeight() / 2));
    }

    @Override
    public int getHeight()
    {
        return region.getRegionHeight() / 2;
    }
}