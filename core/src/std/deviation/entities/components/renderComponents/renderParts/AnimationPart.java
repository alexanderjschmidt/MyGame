package std.deviation.entities.components.renderComponents.renderParts;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class AnimationPart extends RenderPart
{
    public Animation ani;

    public AnimationPart(Animation ani)
    {
        this.ani = ani;
        this.ani.setPlayMode(PlayMode.LOOP);
    }

    @Override
    public void render(Batch batch, float x, float y, float elapsed, PlayMode mode)
    {
        ani.setPlayMode(mode);
        TextureRegion region = ani.getKeyFrame(elapsed);
        batch.draw(region, x - (region.getRegionWidth() / 2), y - (region.getRegionHeight() / 2));
    }

    @Override
    public int getHeight()
    {
        return ani.getKeyFrame(0).getRegionHeight() / 2;
    }

    public boolean isAnimationFinished(float delta)
    {
        return ani.isAnimationFinished(delta);
    }
}