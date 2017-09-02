package std.deviation.entities.components.renderComponents.renderParts;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class RenderPart
{
    public abstract void render(Batch batch, float x, float y, float elapsed, PlayMode mode);

    public abstract int getHeight();

}